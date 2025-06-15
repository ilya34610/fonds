package ru.pssbd.fonds.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${server.address}")
    private String appBaseUrl;

    @Value("${server.port}")
    private String appPort;

    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse resp,
                             Object handler) throws Exception {
        String uri = req.getRequestURI();

        // Разрешить доступ на пути логина/регистрации и на подтверждение QR
        if (uri.startsWith("/login")
                || uri.startsWith("/registration")
                || uri.startsWith("/passwordConfirm")
                || uri.startsWith("/api/qr-login/confirm")) {
            return true;
        }

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            return true;
        }

        resp.sendRedirect("/login");
        return false;
    }
}

