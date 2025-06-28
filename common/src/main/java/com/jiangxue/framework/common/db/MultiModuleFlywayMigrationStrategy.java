package com.jiangxue.framework.common.db;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

import java.util.List;

public class MultiModuleFlywayMigrationStrategy implements FlywayMigrationStrategy {
    protected static final Logger logger = LoggerFactory.getLogger(MultiModuleFlywayMigrationStrategy.class);

    private List<FluentConfiguration> migrations;

    public MultiModuleFlywayMigrationStrategy(List<FluentConfiguration> migrations) {
        this.migrations = migrations;
    }

    @Override
    public void migrate(Flyway flyway) {
        this.migrations.forEach(mig -> {
            Flyway module = mig.dataSource(flyway.getConfiguration().getDataSource()).load();
            try {
                module.repair();
            } catch (FlywayException ex) {
                String schemaTable = mig.getTable();
                logger.error("flyway repair exception,table = {}", schemaTable, ex);
            }
            module.migrate();
        });
    }


}
