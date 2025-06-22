package ru.pssbd.fonds.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.config.MailConfig;
import ru.pssbd.fonds.dto.input.PasswordConfirmDto;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.enums.Role;
import ru.pssbd.fonds.mappers.UserMapper;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

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

    public void sendEmailAndSaveCode(UserOutput userOutput) {


        // 1. SMTP‑параметры
        Properties props = new Properties();
        props.put("mail.smtp.host", mailConfig.getHost());
        props.put("mail.smtp.port", mailConfig.getPort());
        props.put("mail.smtp.auth", mailConfig.getAuth());
        props.put("mail.smtp.starttls.enable", mailConfig.getStarttls());

        // 2. Создание сессии с авторизацией
        Session codeSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getLogin(), mailConfig.getPassword());
            }
        });

        try {
            // 3. Формирование письма
            MimeMessage codeMessage = new MimeMessage(codeSession);
            codeMessage.setFrom(new InternetAddress(mailConfig.getLogin()));
            codeMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userOutput.getLogin())
            );
            codeMessage.setSubject("Код подтверждения вашей почты");
            String code = generateVerificationCode();
            codeMessage.setText("Здравствуйте, " + userOutput.getFio() + "!\n\nВаш код подтверждения: " + code + "\n\nС уважением.");

            // 4. Отправка
//            Transport.send(codeMessage);
            System.out.println("Письмо успешно отправлено. Код: " + code);

            userService.updateCode(userOutput, code);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String textMessage, UserOutput userOutput) {

        // 1. SMTP‑параметры
        Properties props = new Properties();
        props.put("mail.smtp.host", mailConfig.getHost());
        props.put("mail.smtp.port", mailConfig.getPort());
        props.put("mail.smtp.auth", mailConfig.getAuth());
        props.put("mail.smtp.starttls.enable", mailConfig.getStarttls());

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
                    InternetAddress.parse(userOutput.getLogin())
            );
            message.setSubject("Запрос на регистрацию");
            message.setText("Здравствуйте, " + userOutput.getFio() + "!\n\n" + textMessage + " " + "\n\nС уважением.");

            // 4. Отправка
//            Transport.send(message);


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
        // gensalt аргумент — логарифм числа раундов
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }

    // Проверка пароля
    public static boolean verify(String plainPassword, String hashed) {
        return BCrypt.checkpw(plainPassword, hashed);
    }

    // даём возможность логиниться при правильном коде с почты
    public UserEntity userRegistration(UserEntity userEntity, PasswordConfirmDto passwordConfirmDto) {

        Optional<UserEntity> userOpt = userService.getUserByLogin(passwordConfirmDto.getLogin());

        if (userOpt.isPresent()) {
            // для этих ролей подтвержение в интерфейсе через админа
            if (!(Objects.equals(userEntity.getRole().getName(), Role.ADMIN.name()) ||
                    Objects.equals(userEntity.getRole().getName(), Role.FOUNDER.name()) ||
                    Objects.equals(userEntity.getRole().getName(), Role.STAFF.name()))) {
                userEntity.setCanLogin(true);
            }

            userService.save(userEntity);
            return userEntity;
        } else {
            return null;
        }

    }

    public int evaluateDifficultPassword(String password) {
        int length = password.length();
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+[]{}|;:'\",.<>/?".indexOf(ch) >= 0);

        int categories = 0;
        if (hasLower) categories++;
        if (hasUpper) categories++;
        if (hasDigit) categories++;
        if (hasSpecial) categories++;

        if (length >= 12 && categories >= 3) {
            return 3;
        } else if (length >= 8 && categories >= 2) {
            return 2;
        } else {
            return 1;
        }
    }


    public void resendVerificationCode(String login) {

        UserOutput userOutput = userMapper.toOutput(userService.getUserByLogin(login).orElseThrow());

        sendEmailAndSaveCode(userOutput);
    }

}

