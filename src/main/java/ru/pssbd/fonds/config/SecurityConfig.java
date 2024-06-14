package ru.pssbd.fonds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    @Order(1)
//    public Flyway flyway() {
//        return Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/fonds", "postgres", "postgres")
//                .load();
//    }
//
//    @Bean
//    @Order(2)
//    public DataSource dataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:postgresql://localhost:5432/fonds");
//        config.setUsername("client");
//        config.setPassword("client");
//        config.setDriverClassName("org.postgresql.Driver");
//        return new HikariDataSource(config);
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()  // Разрешаем доступ к странице логина и публичным страницам
                .anyRequest().authenticated()  // Все остальные страницы требуют аутентификации
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index", true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")  // Маршрут для logout
                .logoutSuccessUrl("/login")  // Перенаправление после успешного выхода
                .permitAll();
        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println("admin: " + encoder.encode("admin"));
//        System.out.println("staff: " + encoder.encode("staff"));
//        System.out.println("founder: " + encoder.encode("founder"));
//        System.out.println("client: " + encoder.encode("client"));
//        System.out.println("client1: " + encoder.encode("client1"));
//        System.out.println("client2: " + encoder.encode("client2"));
//        System.out.println("donater: " + encoder.encode("donater"));


        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("SELECT login AS username, password, can_login AS enabled FROM public.users WHERE login = ?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.login AS username, r.name AS authority " +
                        "FROM users u " +
                        "INNER JOIN roles r ON u.id_roles = r.id " +
                        "WHERE u.login = ?");

        return manager;
    }


//    @Bean(name = "pwdEncoder")
//    public PasswordEncoder getPasswordEncoder() {
//        DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories
//                .createDelegatingPasswordEncoder();
//        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
//        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
//        return delPasswordEncoder;
//    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        // For production, use BCryptPasswordEncoder or another strong encoder
        return NoOpPasswordEncoder.getInstance();
//        return new Pbkdf2PasswordEncoder("", 185000, 256);
//        return new BCryptPasswordEncoder();
    }


//
//
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService());
//    }
//
//
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .httpBasic();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////        manager.createUser(User.withDefaultPasswordEncoder().username("admin").password("admin").roles("admin").build());
////        manager.createUser(User.withDefaultPasswordEncoder().username("staff").password("staff").roles("staff").build());
////        manager.createUser(User.withDefaultPasswordEncoder().username("founder").password("founder").roles("founder").build());
////        manager.createUser(User.withDefaultPasswordEncoder().username("client").password("client").roles("client").build());
////        manager.createUser(User.withDefaultPasswordEncoder().username("client1").password("client1").roles("client").build());
////        manager.createUser(User.withDefaultPasswordEncoder().username("client2").password("client2").roles("client").build());
////        manager.createUser(User.withDefaultPasswordEncoder().username("donater").password("donater").roles("donater").build());
//        return manager;
//    }
//
//
//































}
