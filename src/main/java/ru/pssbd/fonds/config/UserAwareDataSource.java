//package ru.pssbd.fonds.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class UserAwareDataSource extends AbstractRoutingDataSource {
//
//    @Autowired
//    private DataSource primaryDataSource;
//
//    public UserAwareDataSource(DataSource primaryDataSource) {
//        Map<Object, Object> dataSources = new HashMap<>();
//        dataSources.put(null, primaryDataSource);
//        this.setTargetDataSources(dataSources);
//        this.setDefaultTargetDataSource(primaryDataSource);
//    }
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            return authentication.getName();
//        }
//        return null;
//    }
//
//    @PostConstruct
//    public void init() {
//        Map<Object, Object> dataSources = new HashMap<>();
//        dataSources.put(null, primaryDataSource);
//        this.setTargetDataSources(dataSources);
//        this.setDefaultTargetDataSource(primaryDataSource);
//    }
//
//    public void addUser(String username, String password) {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/fonds");
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//
//        Map<Object, Object> dataSources = new HashMap<>(getResolvedDataSources());
//        dataSources.put(username, dataSource);
//        setTargetDataSources(dataSources);
//        afterPropertiesSet();
//    }
//}
