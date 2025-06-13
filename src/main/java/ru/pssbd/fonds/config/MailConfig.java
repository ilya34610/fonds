package ru.pssbd.fonds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail.smtp")
@Profile("dev")
@Data
public class MailConfig {

    private String login;

    private String password;

    private String host;

    private Integer port;

    private String auth;

    private String starttls;

    private String ssl;

}
