package com.jiangxue.waxberry.fileserver;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileServerFlywayConfiguration{

    @Bean
    public FluentConfiguration fsFlywayMigrationConfig() {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .table("fs_schema_history")
                .locations("classpath:db/migration/fileserver");
    }
}
