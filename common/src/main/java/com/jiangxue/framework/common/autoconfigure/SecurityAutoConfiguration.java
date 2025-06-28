package com.jiangxue.framework.common.autoconfigure;

import com.jiangxue.framework.common.security.BasicSecurityConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

/**
 * Security自动配置
 */
@AutoConfiguration
@ConditionalOnWebApplication
@Import({BasicSecurityConfig.class})
public class SecurityAutoConfiguration {
} 
