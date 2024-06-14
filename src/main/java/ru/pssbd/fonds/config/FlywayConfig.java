//package ru.pssbd.fonds.config;
//
//import org.flywaydb.core.Flyway;
//import org.flywaydb.core.api.configuration.FluentConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//@Configuration
//
//public class FlywayConfig {
//
//    @Bean
//    @DependsOn
//    public Flyway flyway() {
//        return Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/fonds", "postgres", "postgres")
//                .load();
//    }
//
//}
