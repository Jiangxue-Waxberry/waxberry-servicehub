package com.jiangxue.waxberry.auth.config;

import com.jiangxue.waxberry.auth.service.CustomUserDetails;
import com.jiangxue.waxberry.auth.service.RedisService;
import com.jiangxue.waxberry.user.dto.UserDto;
import com.jiangxue.waxberry.user.repository.UserProcessRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * OAuth2授权服务器配置类
 * 配置OAuth2授权服务器的核心组件，包括安全过滤器链、客户端注册、JWT签名密钥等
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    @Value("${auth.issuerUrl}")
    private String issuerUrl;

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler("/login");
    }


    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * 配置授权服务器的安全过滤器链
     * 处理OAuth2授权端点、令牌端点、JWK集端点等核心功能
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,SecurityContextRepository securityContextRepository) throws Exception {
        // 应用OAuth2授权服务器的默认安全配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 启用OpenID Connect支持
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//                .oidc(Customizer.withDefaults());
                .oidc(oidc -> oidc
                        .logoutEndpoint(logout -> logout.errorResponseHandler((req,resp,ex)->{
                                    // 清除登录信息
                                    // 1. 使session失效
                                    if (req.getSession(false) != null) {
                                        req.getSession().invalidate();
                                    }
                                    
                                    // 2. 清除SecurityContext
                                    org.springframework.security.core.context.SecurityContextHolder.clearContext();
                                    
                                    // 3. 重定向到登出后页面
                                    String postLogoutRedirectUri = req.getParameter("post_logout_redirect_uri");
                                    resp.sendRedirect(postLogoutRedirectUri);
                                })));

        // 配置CORS和资源服务器
        http
                //.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 配置JWT资源服务器支持
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()))
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );
        http.securityContext(context -> context.securityContextRepository(securityContextRepository));


        return http.build();
    }

    /**
     * 配置默认的安全过滤器链
     * 处理OAuth2认证和用户管理相关的请求
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository
            ,CustomAuthenticationFailureHandler failureHandler) throws Exception {
        CompositeAuthenticationFilter compositeAuthenticationFilter = new CompositeAuthenticationFilter();
        compositeAuthenticationFilter.setAuthenticationManager(authenticationManager);
        compositeAuthenticationFilter.setSecurityContextRepository(securityContextRepository);

        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/");
        compositeAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);

        compositeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

        http
                .securityMatcher( "/", "/register", "/logout", "/error",
                        "/h2-console/**", "/login")
                //.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        // 允许匿名访问的URL（包含静态资源和登录页面）
                        .requestMatchers(
                                "/oauth2/**",
                                "/register",
                                "/h2-console/**",
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/pkce-client.html", "/oauth2-test.html",
                                //"/auth/admin/users/*",
                                "/auth/users/register",
                                "/",  // 允许访问登录页面
                                "/auth/sms/*"
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/auth/admin/users/*","/auth/sms/*","/auth/users/register", "/", "/login","/oauth2/*","/connect/logout","/.well-known/**" )
                )
                .addFilterBefore(compositeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions().disable());
        // 添加短信认证过滤器
        http.securityContext(context -> context.securityContextRepository(securityContextRepository));
        return http.build();
    }

    /**
     * 配置客户端注册信息
     * 定义授权服务器支持的OAuth2客户端及其权限
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofDays(1))  // 访问令牌有效期30天
                .refreshTokenTimeToLive(Duration.ofDays(1)) // 刷新令牌有效期30天
                .reuseRefreshTokens(true) // 是否重用刷新令牌
                .build();
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        return registeredClientRepository;
    }

    /**
     * 配置JWT签名密钥源
     * 生成RSA密钥对用于签署和验证JWT令牌
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成RSA密钥对
     * 用于JWT的签名和验证
     */
    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * 配置JWT解码器
     * 用于验证和解析JWT令牌
     */
    @Bean
    @Primary
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 配置授权服务器设置
     * 定义授权服务器的发行者URL等核心设置
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder()
//                .build();
        return AuthorizationServerSettings.builder().issuer(issuerUrl)
                .build();
    }

    /**
     * 自定义JWT令牌，添加用户ID等额外声明
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            if ("access_token".equals(context.getTokenType().getValue())) {
                Object principal = context.getPrincipal().getPrincipal();
                if (principal instanceof CustomUserDetails customUserDetails) {
                    var user = customUserDetails.getUser();
                    if (!ObjectUtils.isEmpty(user.getId())) {
                        context.getClaims().claim("userId", user.getId());
                    }
                    if (!ObjectUtils.isEmpty(user.getUsername())) {
                        context.getClaims().claim("userName", user.getUsername());
                    }
                    if (!ObjectUtils.isEmpty(user.getLoginname())) {
                        context.getClaims().claim("loginName", user.getLoginname());
                    }
                    if (!ObjectUtils.isEmpty(user.getMobile())) {
                        context.getClaims().claim("mobile", user.getMobile());
                    }
                    if (!ObjectUtils.isEmpty(user.getEmail())) {
                        context.getClaims().claim("email", user.getEmail());
                    }
                    if (!ObjectUtils.isEmpty(user.getUserRole())) {
                        context.getClaims().claim("role", user.getUserRole());
                    }
                }
            }
            if ("id_token".equals(context.getTokenType().getValue())) {
                Object principal = context.getPrincipal().getPrincipal();
                if (principal  instanceof CustomUserDetails customUserDetails) {
                    var user = customUserDetails.getUser();
                    context.getClaims()
                            .claim("userId", user.getId())
                            .claim("loginName", user.getLoginname())
                            .claim("role", user.getUserRole());
                }
            }
        };
    }


    /**
     * 短信认证提供者
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, RedisService redisService, UserProcessRepository userProcessRepository) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        smsAuthenticationProvider.setUserDetailsService(userDetailsService);
        smsAuthenticationProvider.setRedisServicet(redisService);
        smsAuthenticationProvider.setUserProcessRepository(userProcessRepository);

        return new ProviderManager(List.of(smsAuthenticationProvider,daoAuthenticationProvider ));
    }
}
