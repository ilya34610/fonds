package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pssbd.fonds.constant.Role;
import ru.pssbd.fonds.dto.UserRegistrationDto;
import ru.pssbd.fonds.dto.input.PasswordCheckInput;
import ru.pssbd.fonds.dto.input.PasswordConfirmDto;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthenticationEndpoint {

    private final AuthenticationService authenticationService;

    private final RoleService roleService;

    private final UserService userService;
    private final BackupService backupService;


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

        Optional<UserEntity> userEntityOpt = userService.getUserByLogin(userDto.getLogin());

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            // обработка ситуации, если пользователь до этого уже пытался регистрироваться
            if (userEntity.getLogin().equals(userDto.getLogin())) {
                result.rejectValue(
                        "login",              // поле
                        "password.mismatch",       // код ошибки (можно использовать в messages.properties)
                        "Этот логин уже привязан к учётной записи"      // сообщение
                );
            }

            if (result.hasErrors()) {
                return "registration";
            }
        }


        // 1) Проверяем совпадение паролей
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue(
                    "confirmPassword",        // поле
                    "password.mismatch",       // код ошибки (можно использовать в messages.properties)
                    "Пароли не совпадают"      // сообщение
            );
        }

        // Если базовые аннотации нашли ошибки
        if (result.hasErrors()) {
            return "registration";
        }
        // Вызываем сервисную логику и ловим слабый пароль
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
        // Если есть ошибки, снова показываем форму
        if (result.hasErrors()) {
            return "registration";
        }
        try {
            // отправка сообщения с кодом на почту, добавление пользователя и фиксация кода в БД
            authenticationService.sendEmailAndSaveCode(userDto);

            return "redirect:/passwordConfirm?login=" + URLEncoder.encode(userDto.getLogin(), StandardCharsets.UTF_8);

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
    public ModelAndView passwordConfirm(
            @ModelAttribute("passwordConfirm") @Validated PasswordConfirmDto dto,
            BindingResult result,
            HttpSession session) {

        ModelAndView mav = new ModelAndView();

        // Проверка кода
        Optional<UserEntity> userOpt = userService.getUserByLogin(dto.getLogin());
        if (userOpt.isEmpty() ||
                !Objects.equals(dto.getCode(), userOpt.get().getMailCode())) {

            result.rejectValue(
                    "code",
                    "code.mismatch",
                    "Код указан неверно"
            );
        }

        // Если ошибки — показать форму подтверждения
        if (result.hasErrors()) {
            mav.setViewName("passwordConfirm");
            // Обязательно вернуть dto, чтобы поля и ошибки отобразились
            mav.addObject("passwordConfirm", dto);
            return mav;
        }

        UserEntity user = userOpt.get();
        String roleName = user.getRole().getName();

        // Ветка для ADMIN/STAFF/FOUNDER
        if (Role.ADMIN.name().equals(roleName) ||
                Role.STAFF.name().equals(roleName) ||
                Role.FOUNDER.name().equals(roleName)) {

            authenticationService.userRegistration(user, dto);

            // Готовим форму логина с уведомлением
            mav.setViewName("login");
            mav.addObject("infoMessage",
                    "Запрос на регистрацию отправлен. Пожалуйста, ожидайте подтверждения.");
            return mav;
        }

        // Иначе — обычный пользователь
        authenticationService.userRegistration(user, dto);
        session.setAttribute("currentUser", user);

        // Перенаправляем на главную
        mav.setViewName("redirect:/index");

        return mav;
    }


    // Обработчик запроса проверки сложности пароля
    @PostMapping("/checkingPasswordComplexity")
    public ResponseEntity<Integer> checkingPasswordComplexity(@RequestBody PasswordCheckInput password) {
        if (password == null) {
            return ResponseEntity.badRequest().build();
        }
        int code = authenticationService.evaluateDifficultPassword(password.getPassword());
        // Возвращаем код 1, 2 или 3
        return ResponseEntity.ok(code);
    }


}
