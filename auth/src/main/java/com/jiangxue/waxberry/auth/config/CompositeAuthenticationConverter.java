package com.jiangxue.waxberry.auth.config;

import com.jiangxue.waxberry.auth.dto.SmsAuthenticationToken;
import com.jiangxue.waxberry.auth.utils.AppConfigUtil;
import com.jiangxue.waxberry.auth.utils.RSAUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.PrivateKey;

@Slf4j
public class CompositeAuthenticationConverter implements AuthenticationConverter {

    //private final String usernameParameter = "username";
    private final String email = "email";
    private final String passwordParameter = "password";
    private final String mobileParameter = "mobile";
    private final String codeParameter = "code";
    private final String authTypeParameter = "auth_type";

    public Authentication convert(HttpServletRequest request) {
        String authType = request.getParameter(authTypeParameter);
        if ("sms".equals(authType)) {
            String mobile = request.getParameter(mobileParameter);
            mobile = (mobile != null) ? mobile.trim() : "";
            String code = request.getParameter(codeParameter);
            code = (code != null) ? code : "";
            return new SmsAuthenticationToken(mobile, code);
        }

        // Default to username/password
        //String username = request.getParameter(usernameParameter);
        //username = request.getParameter(mobileParameter);
        String username = request.getParameter(mobileParameter);
        if (ObjectUtils.isEmpty(username)) {
            username = request.getParameter(email);
        }
        username = (username != null) ? username.trim() : "";
        String password = request.getParameter(passwordParameter);

        try {
            PrivateKey privateKey = RSAUtil.getPrivateKey(AppConfigUtil.getPrivateKeyPem());
            password = RSAUtil.decrypt(password, privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        password = (password != null) ? password : "";
        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }
}
