package com.jiangxue.framework.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    /**
     * 是否启用CORS配置
     */
    private Boolean enabled = false;
    
    /**
     * 允许的源，默认为空列表
     */
    private List<String> allowedOrigins = new ArrayList<>();
    
    /**
     * 允许的HTTP方法，默认只允许GET和POST
     */
    private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST"));
    
    /**
     * 允许的请求头，默认为空列表
     */
    private List<String> allowedHeaders = new ArrayList<>();
    
    /**
     * 是否允许发送Cookie，默认为false
     */
    private Boolean allowCredentials = false;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // Getters and Setters
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public Boolean getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }
} 