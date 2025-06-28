package com.jiangxue.framework.common.autoconfigure;

import com.jiangxue.framework.common.properties.CorsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsAutoConfiguration {
    
    private final CorsProperties corsProperties;

    public CorsAutoConfiguration(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cors", name = "enabled", havingValue = "true")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 只有在明确配置了origins时才设置
        if (!corsProperties.getAllowedOrigins().isEmpty()) {
            configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        }
        
        // 设置允许的HTTP方法
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        
        // 只有在明确配置了headers时才设置
        if (!corsProperties.getAllowedHeaders().isEmpty()) {
            configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        }
        
        configuration.setAllowCredentials(corsProperties.getAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
