package com.jiangxue.waxberry.auth;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AuthFlywayConfiguration {
    @Autowired
    DataSource dataSource;

    @Bean
    public FluentConfiguration identityFlywayMigrationConfig() {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .table("auth_schema_history")
                .locations("classpath:db/migration/identity");
    }
}
