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
import ru.pssbd.fonds.service.BackupService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final BackupService backupService;

    // 1. Свойства для primary (url, user, pass, driver)
    @Bean
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    // 2. Сам primaryHikari — с биндингом spring.datasource.primary.hikari.*
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

    // 3. Свойства для standby (url, user, pass, driver)
    @Bean
    @ConfigurationProperties("spring.datasource.standby")
    public DataSourceProperties standbyDataSourceProperties() {
        return new DataSourceProperties();
    }

    // 4. Сам standbyHikari — с биндингом spring.datasource.standby.hikari.*
    @Bean(name = "standbyDataSource")
    @ConfigurationProperties("spring.datasource.standby.hikari")
    public HikariDataSource standbyDataSource(
            @Qualifier("standbyDataSourceProperties") DataSourceProperties props) {
        HikariDataSource ds = props
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        printDataSourceInfo("standby", ds);
        return ds;
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource dynamicDataSource(
            @Qualifier("primaryDataSource") DataSource primary,
            @Qualifier("standbyDataSource") DataSource standby) {
        Map<Object, Object> targets = Map.of(
                "primary", primary,
                "standby", standby
        );
        AbstractRoutingDataSource router = new AbstractRoutingDataSource() {
            private volatile Object lastKey = "primary";
            private volatile long lastCheckTime;

            @Override
            protected Object determineCurrentLookupKey() {
                if (System.currentTimeMillis() - lastCheckTime > 500) {
                    boolean primaryAlive = false;
                    try {
                        primaryAlive = isPrimaryAlive(primary);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Object newKey = primaryAlive ? "primary" : "standby";
                    if (!newKey.equals(lastKey)) {
                        System.out.println("Switching to " + newKey + " at " + new Date());
                        lastKey = newKey;
                    }
                    lastCheckTime = System.currentTimeMillis();
                }
                return lastKey;
            }
        };
        router.setDefaultTargetDataSource(primary);
        router.setTargetDataSources(targets);
        return router;
    }

    private boolean isPrimaryAlive(DataSource dataSource) throws Exception {
        try (Connection c = dataSource.getConnection()) {
            return c.isValid(1);
        } catch (SQLException e) {
            backupService.performBackupAndRestore();
            return false;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private void printDataSourceInfo(String name, HikariDataSource ds) {
        System.out.println("\n=== " + name.toUpperCase() + " DATASOURCE ===");
        System.out.println("JDBC URL: " + ds.getJdbcUrl());
        System.out.println("Username: " + ds.getUsername());
        System.out.println("Driver Class: " + ds.getDriverClassName());
        System.out.println("================================\n");
    }
}
