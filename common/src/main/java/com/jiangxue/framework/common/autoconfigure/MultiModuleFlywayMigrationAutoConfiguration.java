package com.jiangxue.framework.common.autoconfigure;

import com.jiangxue.framework.common.db.MultiModuleFlywayMigrationStrategy;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MultiModuleFlywayMigrationAutoConfiguration {
    @Bean
    public MultiModuleFlywayMigrationStrategy multiModuleFlywayMigrationStrategy(List<FluentConfiguration> migrations) {
        return new MultiModuleFlywayMigrationStrategy(migrations);
    }
}
