package com.jiangxue.waxberry.auth.config;

import com.jiangxue.waxberry.auth.utils.AppConfigUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AppConfigLoader {
    @Value("${auth.privateKey}")
    private String privateKeyPem;

    @PostConstruct
    public void init() {
        AppConfigUtil.setPrivateKeyPem(privateKeyPem);
    }
}
