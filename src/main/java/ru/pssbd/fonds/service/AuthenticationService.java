package ru.pssbd.fonds.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.config.MailConfig;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.mappers.UserMapper;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JdbcTemplate jdbcTemplate;
    private final MailConfig mailConfig;
    private final UserService userService;
    private final UserMapper userMapper;




    public UserEntity authenticate(String username, String rawPassword) {
        UserEntity user = jdbcTemplate.queryForObject(
                "SELECT id, login, password, can_login FROM users WHERE login = ?",
                BeanPropertyRowMapper.newInstance(UserEntity.class),
                username);

        if (user == null || !user.getCanLogin()) {
            throw new RuntimeException("Пользователь не найден или заблокирован");
        }

        if (!verify(rawPassword, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        return user;

    }

    public void passwordValidation(UserInput userInput) {
        if (!isPasswordValid(userInput.getPassword())) {
            throw new IllegalArgumentException("Пароль должен быть минимум 8 символов, содержать цифру, " +
                    "прописную и строчную букву, а также специальный символ");
        }
    }

    /**
     * Проверяет, что пароль:
     * - минимум 8 символов
     * - содержит хотя бы одну цифру [0-9]
     * - содержит хотя бы одну строчную букву [a-z]
     * - содержит хотя бы одну прописную букву [A-Z]
     * - содержит хотя бы один спецсимвол из @#$%^&+=
     */
    private boolean isPasswordValid(String password) {
        String pattern = "^(?=.*[0-9])" +         // хотя бы одна цифра
                "(?=.*[a-z])" +         // хотя бы одна строчная буква
                "(?=.*[A-Z])" +         // хотя бы одна прописная буква
                "(?=.*[@!#$%^&+=])" +    // хотя бы один спецсимвол
                ".{8,}$";               // не менее 8 символов
        return password != null && password.matches(pattern);
    }

    public void sendEmailAndSaveCode(UserInput userInput) {
        //В любом случае фиксируем регистрацию пользователя в базе данных
        userInput.setCanLogin(false);
        userInput.setPassword(hash(userInput.getPassword()));
        userService.save(userMapper.fromInput(userInput));


        // 1. SMTP‑параметры
        Properties props = new Properties();
        props.put("mail.smtp.host", mailConfig.getHost());
        props.put("mail.smtp.port", mailConfig.getPort());
        props.put("mail.smtp.auth", mailConfig.getAuth());
        props.put("mail.smtp.starttls.enable", mailConfig.getStarttls());  // TLS :contentReference[oaicite:3]{index=3}
//         props.put("mail.smtp.ssl.enable", "true");    // SSL (порт 465)

        // 2. Создание сессии с авторизацией
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getLogin(), mailConfig.getPassword());
            }
        });

        try {
            // 3. Формирование письма
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getLogin()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userInput.getLogin())
            );
            message.setSubject("Код подтверждения вашей почты");
            String code = generateVerificationCode();
            message.setText("Добрый день " + userInput.getFio() + "!\n\nВаш код подтверждения: " + code + "\n\nС уважением.");

            // 4. Отправка
            //На всякий случай убираем, чтобы не спамить
//            Transport.send(message);
            System.out.println("Письмо успешно отправлено. Код: " + code);



            userService.updateCode(userInput,code);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(1_000_000);         // [0, 999999]
        return String.format("%06d", code);           // добавляем ведущие нули
    }

    public static String hash(String plainPassword) {
        // gensalt аргумент — логарифм числа раундов (рекомендуется 10–12)
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }

    // Проверка пароля
    public static boolean verify(String plainPassword, String hashed) {
        return BCrypt.checkpw(plainPassword, hashed);
    }

}

