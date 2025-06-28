package com.jiangxue.framework.common.autoconfigure;

import com.jiangxue.framework.common.web.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;

public class ExceptionHandlerAutoConfiguration {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
