package com.jiangxue.framework.common.autoconfigure;

import com.jiangxue.framework.common.security.SecurityProperties;
import com.jiangxue.framework.common.security.XSSFilter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
public class BasicSecurityAutoConfiguration {


    @Bean
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, SecurityProperties securityProperties) throws Exception {
        http
                // CSRF 配置
                .csrf(csrf -> {
                    if (securityProperties.isCsrfEnabled()) {
                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringRequestMatchers(securityProperties.getCsrfIgnorePaths().toArray(new String[0]));
                    } else {
                        log.info("CSRF protection disabled");
                        csrf.disable();
                    }
                })

                // XSS 防护 - 配置 HttpSecurity Headers
                .headers(headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives(securityProperties.getCspPolicy())));

        return http.build();
    }

//    @Bean
    public FilterRegistrationBean<XSSFilter> xssFilterRegistrationBean() {
        FilterRegistrationBean<XSSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XSSFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
