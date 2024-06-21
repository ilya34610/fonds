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
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.service.UserService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final UserMapper userMapper;

//    @Autowired
//    private UserAwareDataSource userAwareDataSource;

    private final DataSource userDataSource;


    public SecurityConfig(UserService userService, UserMapper userMapper, DataSource userDataSource) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userDataSource = userDataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index", true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT login AS username, password, can_login AS enabled FROM public.users WHERE login = ?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.login AS username, r.name AS authority " +
                        "FROM users u " +
                        "INNER JOIN roles r ON u.id_roles = r.id " +
                        "WHERE u.login = ?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                                Authentication authentication) throws IOException, ServletException {
//                String username = authentication.getName();
//                String password = getUserPostgresPassword(username);
//                userAwareDataSource.addUser(username, password);
//                response.sendRedirect("/index");
//            }
//        };
//    }
//
//    private String getUserPostgresPassword(String username) {
//        // Здесь вам нужно реализовать логику получения пароля PostgreSQL для пользователя
//        // Это может быть из базы данных или другого безопасного источника
//        // Для примера, возвращаем фиксированный пароль
//
//        UserOutput userOutput = userService.getRoleByLogin(username);
//
//        return userOutput.getPassword();
////        return "userOutput.getPassword()";
//    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/login").permitAll()  // Разрешаем доступ к странице логина и публичным страницам
//                .anyRequest().authenticated()  // Все остальные страницы требуют аутентификации
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/index", true)
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/logout")  // Маршрут для logout
//                .logoutSuccessUrl("/login")  // Перенаправление после успешного выхода
//                .permitAll();
//        return http.build();
//
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//
//        manager.setUsersByUsernameQuery("SELECT login AS username, password, can_login AS enabled FROM public.users WHERE login = ?");
//        manager.setAuthoritiesByUsernameQuery(
//                "SELECT u.login AS username, r.name AS authority " +
//                        "FROM users u " +
//                        "INNER JOIN roles r ON u.id_roles = r.id " +
//                        "WHERE u.login = ?");
//
//        return manager;
//    }
//
//
//
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // For production, use BCryptPasswordEncoder or another strong encoder
//        return NoOpPasswordEncoder.getInstance();
////        return new Pbkdf2PasswordEncoder("", 185000, 256);
////        return new BCryptPasswordEncoder();
//    }

}
