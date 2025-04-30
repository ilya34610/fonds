package ru.pssbd.fonds.endpoint;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthenticationEndpoint {

    private final AuthService authService;

    public AuthenticationEndpoint(AuthService authService) {
        this.authService = authService;
    }

    //вызывается при переходе на страницу авторизации пользователя
    @GetMapping("/login")
    public String login() {
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
            UserEntity user = authService.authenticate(username, password);
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
//        model.addAttribute("user", new UserRegistrationDto());
        return "registration";   // файл src/main/resources/templates/registration.html
    }

//    @PostMapping("/registration")
//    public String registerUser(@ModelAttribute("user") @Validated UserRegistrationDto userDto,
//                               BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "registration";
//        }
//        // здесь ваш UserService.registerNewUser(userDto);
//        return "redirect:/login?success";
//    }

}
