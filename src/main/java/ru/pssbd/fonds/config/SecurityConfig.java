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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/admin/**").hasAuthority("ADMIN")
//                .antMatchers("/staff/**").hasAnyAuthority("ADMIN", "STAFF")
//                .antMatchers("/founder/**").hasAnyAuthority("ADMIN", "FOUNDER")
//                .antMatchers("/user/**").authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//        return http.build();

        http
                .csrf().disable()
//                .csrfTokenRepository(csrfTokenRepository())
//                .and()
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
//                .and().csrf().disable();
        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("SELECT login AS username, password, can_login AS enabled FROM users WHERE login = ?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.login AS username, r.name AS authority " +
                        "FROM users u " +
                        "INNER JOIN roles r ON u.id_roles = r.id " +
                        "WHERE u.login = ?");

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // For production, use BCryptPasswordEncoder or another strong encoder
        return NoOpPasswordEncoder.getInstance();
    }

//    // Configure the CSRF token repository
//    private CsrfTokenRepository csrfTokenRepository() {
//        // Create a new HttpSessionCsrfTokenRepository
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        // Set the session attribute name for the CSRF token
//        repository.setSessionAttributeName("_csrf");
//        // Return the repository
//        return repository;
//    }

}
