//package ru.pssbd.fonds.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class FlywayConfig {
//
//    @Bean
//    public Flyway flyway(@Qualifier("dataSource") DataSource dataSource) {
//        return Flyway.configure()
//                .dataSource(dataSource)
//                .locations("classpath:db/migration")
//                .load();
//    }
//}