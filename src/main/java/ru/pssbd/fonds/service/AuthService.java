package ru.pssbd.fonds.service;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.entity.UserEntity;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class AuthService {
    private final JdbcTemplate jdbcTemplate;

    public UserEntity authenticate(String username, String rawPassword) {
        UserEntity user = jdbcTemplate.queryForObject(
                "SELECT id, login, password, can_login FROM users WHERE login = ?",
                BeanPropertyRowMapper.newInstance(UserEntity.class),
                username);

        if (user == null || !user.getCanLogin()) {
            throw new RuntimeException("Пользователь не найден или заблокирован");
        }

        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        return user;


    }
}

