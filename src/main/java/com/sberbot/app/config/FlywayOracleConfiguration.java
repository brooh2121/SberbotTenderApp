package com.sberbot.app.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayOracleConfiguration {

    @Autowired
    @Qualifier("oracleTend")
    private DataSource oracleTend;

    @Bean
    public Flyway flywayOracle(DataSource oracleTend){
        return Flyway
                .configure().dataSource(oracleTend)
                .locations("classpath:/db/oracle")
                .baselineOnMigrate(true)
                //.table("oracle_schema_version")
                .validateOnMigrate(true)
                .outOfOrder(true)
                .load();
    }
}
