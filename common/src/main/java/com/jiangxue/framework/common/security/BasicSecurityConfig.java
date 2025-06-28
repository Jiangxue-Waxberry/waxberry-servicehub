package com.jiangxue.framework.common.security;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础安全配置，配置网关用户信息过滤器
 */
@Configuration
@EnableWebSecurity
@ConditionalOnWebApplication
public class BasicSecurityConfig {

    @Bean
    @Order(3)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(nonValidatingJwtDecoder()))
                );

        return http.build();
    }

    @Bean
    public JwtDecoder nonValidatingJwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                try {
                    // 解析JWT但不验证签名
                    SignedJWT signedJWT = SignedJWT.parse(token);

                    // 提取header和payload
                    Map<String, Object> headers = new HashMap<>(signedJWT.getHeader().toJSONObject());
                    Map<String, Object> claims = new HashMap<>(signedJWT.getJWTClaimsSet().getClaims());

                    // 构建Jwt对象
                    Instant issuedAt = signedJWT.getJWTClaimsSet().getIssueTime() != null
                            ? signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                            : Instant.now();

                    Instant expiresAt = signedJWT.getJWTClaimsSet().getExpirationTime() != null
                            ? signedJWT.getJWTClaimsSet().getExpirationTime().toInstant()
                            : null;

                    return new Jwt(
                            token,
                            issuedAt,
                            expiresAt,
                            headers,
                            claims
                    );

                } catch (Exception e) {
                    throw new JwtException("无法解析JWT token: " + e.getMessage(), e);
                }
            }
        };
    }

}
