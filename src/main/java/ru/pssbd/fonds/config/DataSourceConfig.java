//package ru.pssbd.fonds.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
//            @Override
//            protected Object determineCurrentLookupKey() {
//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                if (authentication != null && authentication.isAuthenticated()) {
//                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//                    return userDetails.getUsername();
//                }
//                return null;
//            }
//        };
//
//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put("admin", createDataSource("admin", "password"));
//        targetDataSources.put("staff", createDataSource("staff", "password"));
//        targetDataSources.put("founder", createDataSource("founder", "password"));
//        targetDataSources.put("user", createDataSource("user", "password"));
//        targetDataSources.put("donater", createDataSource("donater", "password"));
//
//        routingDataSource.setTargetDataSources(targetDataSources);
//        routingDataSource.setDefaultTargetDataSource(createDataSource("defaultUser", "defaultPassword"));
//
//        return routingDataSource;
//    }
//
//    private DataSource createDataSource(String username, String password) {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/fonds");
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//
//}
