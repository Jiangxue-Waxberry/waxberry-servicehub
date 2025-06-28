package com.jiangxue.waxberry.manager.system;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfigLoader implements CommandLineRunner {

    @Value("${manage.sandbox-url}")
    private String sandBoxUrl;

    @Value("${manage.plugin-url}")
    private String pluginUrl;

    @Value("${manage.fileserver-url}")
    private String fileServerUrl;


    @Value("${manage.core-url}")
    private String coreUrl;


    // 可以继续添加更多需要加载的配置

    @Override
    public void run(String... args) throws Exception {
        // 将配置设置到系统属性中
        System.setProperty("coreUrl", coreUrl);
        System.setProperty("sandBoxUrl", sandBoxUrl);
        System.setProperty("pluginUrl", pluginUrl);
        System.setProperty("fileServerUrl", fileServerUrl);
        // 可以添加日志记录，确认配置已加载
        log.info("配置已加载到系统属性中");
        log.info("coreUrl: " + System.getProperty("coreUrl"));
        log.info("pluginUrl: " + System.getProperty("pluginUrl"));
        log.info("sandBoxUrl: " + System.getProperty("sandBoxUrl"));
        log.info("fileServerUrl: " + System.getProperty("fileServerUrl"));
    }
}
