package ru.pssbd.fonds.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import ru.pssbd.fonds.service.RecoveryService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final RecoveryService recoveryService;

    @Bean
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "primaryDataSource")
    @ConfigurationProperties("spring.datasource.primary.hikari")
    public HikariDataSource primaryDataSource(
            @Qualifier("primaryDataSourceProperties") DataSourceProperties props) {
        HikariDataSource ds = props
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        printDataSourceInfo("primary", ds);
        return ds;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.standby")
    public DataSourceProperties standbyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource(
            @Qualifier("primaryDataSource") HikariDataSource primaryDs,
            @Qualifier("standbyDataSourceProperties") DataSourceProperties standbyProps) {
        int validationTimeoutSeconds = 1; // можно вынести в props
        return new FailoverDataSource(primaryDs, standbyProps, validationTimeoutSeconds, recoveryService);
    }

    private void printDataSourceInfo(String name, HikariDataSource ds) {
        System.out.println("\n=== " + name.toUpperCase() + " DATASOURCE ===");
        System.out.println("JDBC URL: " + ds.getJdbcUrl());
        System.out.println("Username: " + ds.getUsername());
        System.out.println("Driver Class: " + ds.getDriverClassName());
        System.out.println("================================\n");
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

