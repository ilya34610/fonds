package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pssbd.fonds.dto.UserRegistrationDto;
import ru.pssbd.fonds.dto.input.PasswordConfirmDto;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.service.AuthenticationService;
import ru.pssbd.fonds.service.RoleService;
import ru.pssbd.fonds.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AuthenticationEndpoint {

    private final AuthenticationService authenticationService;

    private final RoleService roleService;

    private final UserService userService;

    //вызывается при переходе на страницу авторизации пользователя
    @GetMapping("/login")
    public String login() {
//        String salt = BCrypt.gensalt(12);
//        String admin = BCrypt.hashpw("admin", salt);
//        String staff = BCrypt.hashpw("staff", salt);
//        String founder = BCrypt.hashpw("founder", salt);
//        String client = BCrypt.hashpw("client", salt);
//        String client1 = BCrypt.hashpw("client1", salt);
//        String client2 = BCrypt.hashpw("client2", salt);
//        String donater = BCrypt.hashpw("donater", salt);


        return "login";
    }

    //вызывается при выходе из профиля
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    //вызывается при авторизации
    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        try {
            UserEntity user = authenticationService.authenticate(username, password);
            session.setAttribute("currentUser", user);
            return "redirect:/index";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    // Обработчик страницы регистрации
    @GetMapping("/registration")
    public String registrationForm(Model model) {

        model.addAttribute("user", new UserRegistrationDto((short) 1));
        model.addAttribute("roles", roleService.getAllElem());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Validated UserInput userDto,
                               BindingResult result, Model model) {
        model.addAttribute("roles", roleService.getAllElem());

        // обработка ситуации, если пользователь до этого уже пытался регистрироваться
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue(
                    "login",              // поле
                    "password.mismatch",       // код ошибки (можно использовать в messages.properties)
                    "Вы уже пытались зарегистрироваться"      // сообщение
            );
        }


        // 1) Проверяем совпадение паролей
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue(
                    "confirmPassword",        // поле
                    "password.mismatch",       // код ошибки (можно использовать в messages.properties)
                    "Пароли не совпадают"      // сообщение
            );
        }

        // 2) Если базовые аннотации нашли ошибки
        if (result.hasErrors()) {
            return "registration";
        }

        // 3) Вызываем сервисную логику и ловим слабый пароль
        try {
            authenticationService.passwordValidation(userDto);
        } catch (IllegalArgumentException ex) {
            // Сообщение из исключения попадёт под поле password
            result.rejectValue(
                    "password",
                    "password.weak",
                    ex.getMessage()
            );
        }

        // 4) Если теперь есть ошибки, снова показываем форму
        if (result.hasErrors()) {
            return "registration";
        }

        try {
            if (userDto.getRole() == 4 || userDto.getRole() == 5) {
                // отправка сообщения с кодом на почту, добавление пользователя и фиксация кода в БД
                authenticationService.sendEmailAndSaveCode(userDto);

                return "redirect:/passwordConfirm?login=" + URLEncoder.encode(userDto.getLogin(), StandardCharsets.UTF_8);
            } else {
                //отправка запроса на регистрацию администратору
            }
        } catch (IllegalArgumentException ex) {
            result.rejectValue(
                    "password",
                    "password.weak",
                    ex.getMessage()
            );
        }


        // 5) Всё ок — редирект на страницу входа
        return "redirect:/login?success";
    }

    // Обработчик страницы подтверждения пароля

    @GetMapping("/passwordConfirm")
    public String passwordConfirm(@RequestParam("login") String login, Model model) {
        model.addAttribute("passwordConfirm", new PasswordConfirmDto(login));
        return "passwordConfirm";
    }


    @PostMapping("/passwordConfirm")
    public String passwordConfirm(@ModelAttribute("passwordConfirm") @Validated PasswordConfirmDto passwordConfirmDto,
                                  BindingResult result,
                                  HttpSession session, Model model) {

//        model.addAttribute("passwordConfirm", new PasswordConfirmDto(passwordConfirmDto.getLogin()));

        if (!Objects.equals(passwordConfirmDto.getCode(),
                userService.getUserByLogin(passwordConfirmDto.getLogin()).getMailCode())) {
            result.rejectValue(
                    "code",        // поле
                    "password.mismatch",       // код ошибки (можно использовать в messages.properties)
                    "Код указан неверно"      // сообщение
            );
        }

//      Если теперь есть ошибки, снова показываем форму
        if (result.hasErrors()) {
            return "passwordConfirm";
        } else {
//        Если код правильный то регистрация
            try {
                UserEntity userEntity = userService.getUserByLogin(passwordConfirmDto.getLogin());
                session.setAttribute("currentUser", userEntity);
                return "redirect:/index";

            } catch (Exception ex) {
                model.addAttribute("error", ex.getMessage());
                return "login";
            }

//            session.setAttribute("currentUser", user);
        }


//        model.addAttribute("passwordConfirm", new PasswordConfirmDto(login));
    }


//    @GetMapping("/registration")
//    public String registrationForm(Model model) {
//
//        model.addAttribute("user", new UserRegistrationDto((short)1));
//        model.addAttribute("roles", roleService.getAllElem());
//        return "registration";
//    }

}
