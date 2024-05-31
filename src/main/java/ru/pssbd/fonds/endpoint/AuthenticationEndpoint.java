package ru.pssbd.fonds.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationEndpoint {

    //вызывается при переходе на страницу авторизации пользователя
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
