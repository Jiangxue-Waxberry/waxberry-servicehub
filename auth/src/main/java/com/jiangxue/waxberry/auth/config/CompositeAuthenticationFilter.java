package com.jiangxue.waxberry.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

public class CompositeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationConverter authenticationConverter = new CompositeAuthenticationConverter();

    public CompositeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Authentication authRequest = authenticationConverter.convert(request);

        if (ObjectUtils.isEmpty(authRequest)) {
            return null;
        }

        return this.getAuthenticationManager().authenticate(authRequest);
    }
} 