package com.sberbot.app.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class FlywayPostgresConfiguration {

    @Autowired
    @Qualifier("postgeTend")
    private DataSource postgeTend;

    @Bean
    public Flyway flywayPostgres(DataSource postgeTend) {
        return Flyway.configure()
                .dataSource(postgeTend)
                .locations("classpath:/db/migration")
                .baselineOnMigrate(true)
                //.table("postgres_schema_version")
                .validateOnMigrate(true)
                .outOfOrder(true)
                .load();
    }

    @Bean
    int migratePostgres(Flyway flyway) {
        return flywayPostgres(postgeTend).migrate();
    }
}
