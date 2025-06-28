package com.jiangxue.waxberry.manager;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagerFlywayConfiguration {

    @Bean
    public FluentConfiguration mgrFlywayMigrationConfig() {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .table("mgr_schema_history")
                .locations("classpath:db/migration/manager");
    }
}
