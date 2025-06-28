package com.jiangxue.framework.common.autoconfigure;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Slf4j
@Configuration
public class LocaleConfiguration implements InitializingBean {
    @Value("${app.timezone:Asia/Shanghai}")
    private String timeZone;

    @Override
    public void afterPropertiesSet() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        log.info("Set default timezone to {}", timeZone);
    }
}
