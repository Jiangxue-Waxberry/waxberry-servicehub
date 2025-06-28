package com.jiangxue.waxberry.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2AuthenticationGatewayFilterFactory 
    extends AbstractGatewayFilterFactory<OAuth2AuthenticationGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationGatewayFilterFactory.class);
    
    @Autowired
    private ReactiveJwtDecoder jwtDecoder;
    
    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @Autowired
    private ObjectMapper objectMapper;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public OAuth2AuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            String method = request.getMethod().toString();
            
            logger.info("=== OAuth2 Filter START === {} {}", method, path);
            logger.debug("Request headers: {}", request.getHeaders().toSingleValueMap());
            
            // 检查是否在白名单中
            if (isPathInWhitelist(path, config.getWhitelistPaths())) {
                logger.info("🔓 Path {} is in whitelist, skipping OAuth2 authentication", path);
                return chain.filter(exchange);
            }
            
            // 提取 token
            String token = extractToken(request);
            if (!StringUtils.hasText(token)) {
                logger.warn("❌ No token found in request to: {} {}", method, path);
                logger.debug("Authorization header: {}", request.getHeaders().getFirst("Authorization"));
                logger.debug("Query params: {}", request.getQueryParams());
                return handleUnauthorized(exchange, "Missing access token");
            }
            
            logger.info("✅ Token extracted successfully for: {} {}, token length: {}", method, path, token.length());
            logger.debug("Token preview: {}...{}", token.substring(0, Math.min(20, token.length())), 
                        token.length() > 20 ? token.substring(token.length() - 10) : "");
            
            // 验证 token
            return validateToken(token)
                .flatMap(isValid -> {
                    if (isValid) {
                        logger.info("✅ Token validation successful for: {} {}", method, path);
                        // Token 有效，先添加用户信息到请求头，然后继续过滤链
                        ServerWebExchange modifiedExchange = addUserInfoToRequestSync(exchange, token);
                        logger.info("=== OAuth2 Filter END === {} {} - AUTHORIZED", method, path);
                        return chain.filter(modifiedExchange);
                    } else {
                        logger.warn("❌ Token validation failed for: {} {}", method, path);
                        return handleUnauthorized(exchange, "Invalid access token");
                    }
                })
                .onErrorResume(throwable -> {
                    logger.error("💥 Token validation error for: {} {} - {}", method, path, throwable.getMessage(), throwable);
                    return handleUnauthorized(exchange, "Token validation failed");
                });
        };
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isPathInWhitelist(String requestPath, List<String> whitelistPaths) {
        if (whitelistPaths == null || whitelistPaths.isEmpty()) {
            return false;
        }
        
        for (String whitelistPath : whitelistPaths) {
            if (pathMatcher.match(whitelistPath, requestPath)) {
                logger.debug("✅ Path {} matches whitelist pattern: {}", requestPath, whitelistPath);
                return true;
            }
        }
        
        logger.debug("❌ Path {} does not match any whitelist pattern", requestPath);
        return false;
    }

    private String extractToken(ServerHttpRequest request) {
        logger.debug("🔍 Extracting token from request...");
        
        // 从 Authorization header 获取 Bearer token
        String authorization = request.getHeaders().getFirst("Authorization");
        logger.debug("Authorization header present: {}", authorization != null);
        
        if (StringUtils.hasText(authorization)) {
            logger.debug("Authorization header value: {}", authorization.substring(0, Math.min(20, authorization.length())) + "...");
            if (authorization.startsWith("Bearer ")) {
                logger.debug("✅ Bearer token found in Authorization header");
                return authorization.substring(7);
            } else {
                logger.debug("❌ Authorization header doesn't start with 'Bearer '");
            }
        }
        
        // 从 query parameter 获取 token
        String tokenParam = request.getQueryParams().getFirst("access_token");
        logger.debug("Query parameter 'access_token' present: {}", tokenParam != null);
        
        if (StringUtils.hasText(tokenParam)) {
            logger.debug("✅ Token found in query parameter");
            return tokenParam;
        }
        
        logger.debug("❌ No token found in request");
        return null;
    }

    private Mono<Boolean> validateToken(String token) {
        // 先从 Redis 缓存中检查
        String cacheKey = "gateway:token:" + token.hashCode();
        logger.debug("🔍 Checking token validation cache, key: {}", cacheKey);
        
        return redisTemplate.opsForValue().get(cacheKey)
            .cast(String.class)
            .doOnNext(cachedValue -> logger.debug("📋 Found cached validation result: {}", cachedValue))
            .map(cachedValue -> {
                boolean isValid = "valid".equals(cachedValue);
                logger.info("💾 Using cached token validation result: {}", isValid ? "VALID" : "INVALID");
                return isValid;
            })
            .switchIfEmpty(
                // 缓存中没有，进行实际验证
                performTokenValidation(token)
                    .doOnSubscribe(subscription -> logger.debug("📋 No cached result found, performing actual token validation"))
                    .flatMap(isValid -> {
                        // 将验证结果缓存
                        String cacheValue = isValid ? "valid" : "invalid";
                        logger.debug("💾 Caching token validation result: {} for key: {}", cacheValue, cacheKey);
                        return redisTemplate.opsForValue()
                            .set(cacheKey, cacheValue, Duration.ofMinutes(5))
                            .doOnSuccess(success -> logger.debug("✅ Token validation result cached successfully"))
                            .doOnError(error -> logger.error("❌ Failed to cache token validation result: {}", error.getMessage()))
                            .thenReturn(isValid);
                    })
            );
    }

    private Mono<Boolean> performTokenValidation(String token) {
        logger.debug("🔐 Starting JWT token validation...");
        
        // 使用 JWT 解码器验证 token
        return jwtDecoder.decode(token)
            .doOnSuccess(jwt -> logger.debug("✅ JWT decoded successfully"))
            .map(jwt -> {
                logger.debug("🕐 Checking token expiration...");
                // 检查 token 是否过期
                if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(java.time.Instant.now())) {
                    logger.warn("❌ Token expired at: {}", jwt.getExpiresAt());
                    return false;
                }
                logger.debug("✅ Token not expired, expires at: {}", jwt.getExpiresAt());
                
                // 检查必要的声明
                String subject = jwt.getSubject();
                logger.debug("👤 Token subject: {}", subject);
                if (!StringUtils.hasText(subject)) {
                    logger.warn("❌ Token missing subject");
                    return false;
                }
                
                // 记录token的其他信息
                logger.debug("📝 Token claims - issuer: {}, audience: {}", jwt.getIssuer(), jwt.getAudience());
                logger.info("✅ Token validated successfully for user: {}", subject);
                return true;
            })
            .onErrorResume(throwable -> {
                logger.error("❌ JWT validation failed: {} - {}", throwable.getClass().getSimpleName(), throwable.getMessage());
                logger.debug("JWT validation error details:", throwable);
                return Mono.just(false);
            });
    }

    private ServerWebExchange addUserInfoToRequestSync(ServerWebExchange exchange, String token) {
        logger.debug("👤 Adding user info to request headers...");
        
        try {
            Jwt jwt = jwtDecoder.decode(token).block();
            if (jwt == null) {
                logger.warn("❌ Failed to decode JWT for user info extraction");
                return exchange;
            }
            
            logger.debug("✅ JWT decoded for user info extraction");
            
            // 从 JWT 中提取用户信息并添加到请求头
            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
            int headerCount = 0;
            
            if (exchange.getRequest().getHeaders().getFirst("Authorization") == null) {
                requestBuilder.header("Authorization", "Bearer " + token);
                logger.debug("📝 Added Authorization header with Bearer token");
                headerCount++;
            }
            
            String username = jwt.getSubject();
            if (StringUtils.hasText(username)) {
                requestBuilder.header("X-User-Name", username);
                logger.debug("📝 Added header X-User-Name: {}", username);
                headerCount++;
            }

            String userId = jwt.getClaimAsString("userId");
            if (StringUtils.hasText(userId)) {
                requestBuilder.header("X-User-Id", userId);
                logger.debug("📝 Added header X-User-Id: {}", userId);
                headerCount++;
            }
            
            // 添加用户权限信息
            Object scopes = jwt.getClaim("scope");
            if (scopes != null) {
                requestBuilder.header("X-User-Scopes", scopes.toString());
                logger.debug("📝 Added header X-User-Scopes: {}", scopes);
                headerCount++;
            }
            
            // 创建新的请求和exchange
            ServerHttpRequest newRequest = requestBuilder.build();
            logger.info("✅ Successfully added {} headers (including token) to request for downstream services", headerCount);
            
            return exchange.mutate().request(newRequest).build();
            
        } catch (Exception e) {
            logger.error("❌ Error adding user info to request: {}", e.getMessage(), e);
            return exchange;
        }
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().toString();
        
        logger.warn("🚫 Returning 401 Unauthorized for: {} {} - {}", method, path, message);
        
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "unauthorized");
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("path", path);
        errorResponse.put("method", method);
        
        try {
            String responseBody = objectMapper.writeValueAsString(errorResponse);
            logger.debug("📝 Unauthorized response body: {}", responseBody);
            DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            logger.error("❌ Error writing unauthorized response: {}", e.getMessage(), e);
            return response.setComplete();
        }
    }

    public static class Config {
        // 配置类，可以添加配置参数
        private boolean enabled = true;
        private List<String> whitelistPaths = new ArrayList<>();
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public List<String> getWhitelistPaths() {
            return whitelistPaths;
        }
        
        public void setWhitelistPaths(List<String> whitelistPaths) {
            this.whitelistPaths = whitelistPaths != null ? whitelistPaths : new ArrayList<>();
        }
    }
} 
