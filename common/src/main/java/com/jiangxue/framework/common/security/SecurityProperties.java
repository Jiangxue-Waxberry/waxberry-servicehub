package com.jiangxue.framework.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private String cspPolicy = "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self'";
    private boolean csrfEnabled = true;
    private List<String> csrfIgnorePaths = List.of();

    public String getCspPolicy() {
        return cspPolicy;
    }

    public void setCspPolicy(String cspPolicy) {
        this.cspPolicy = cspPolicy;
    }

    public boolean isCsrfEnabled() {
        return csrfEnabled;
    }

    public void setCsrfEnabled(boolean csrfEnabled) {
        this.csrfEnabled = csrfEnabled;
    }

    public List<String> getCsrfIgnorePaths() {
        return csrfIgnorePaths;
    }

    public void setCsrfIgnorePaths(List<String> csrfIgnorePaths) {
        this.csrfIgnorePaths = csrfIgnorePaths;
    }
}
