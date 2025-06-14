package ru.pssbd.fonds.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class MvcConfig implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public MvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(
                        "/css/**",
                        "/js/**",
                        "/photo/**",       // если есть картинки
                        "/favicon.ico",
                        "/checkingPasswordComplexity",
                        "/login", "/registration", "/passwordConfirm"
                );
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/photo/**")
                .addResourceLocations("classpath:/static/photo/");
    }

}