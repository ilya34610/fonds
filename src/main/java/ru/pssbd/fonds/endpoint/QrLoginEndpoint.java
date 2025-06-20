package ru.pssbd.fonds.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.pssbd.fonds.entity.QrLoginTokenEntity;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.service.QrLoginTokenService;
import ru.pssbd.fonds.utils.QrCodeGenerator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/qr-login")
@RequiredArgsConstructor
public class QrLoginEndpoint {

    private final QrLoginTokenService qrLoginTokenService;
    private final QrCodeGenerator qrCodeGenerator;



    @Value("${server.address}")
    private String appBaseUrl;

    @Value("${server.port}")
    private String appPort;

//    public QrLoginEndpoint(QrLoginTokenService qrLoginTokenService,
//                           QrCodeGenerator qrCodeGenerator,
//                           JwtService jwtService) {
//        this.qrLoginTokenService = qrLoginTokenService;
//        this.qrCodeGenerator = qrCodeGenerator;
//        this.jwtService = jwtService;
//    }

    /**
     * Генерация QR: клиент (уже аутентифицированный) вызывает этот endpoint
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateQr(HttpSession session) {
        // Получаем пользователя из сессии
        UserEntity user = (UserEntity) session.getAttribute("currentUser");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        QrLoginTokenEntity tokenEntity = qrLoginTokenService.createTokenForUser(user);
        String token = tokenEntity.getToken();
        String loginUrl = "http://" + appBaseUrl + ":" + appPort + "/api/qr-login/confirm?token=" + token;

        try {
            BufferedImage qrImage = qrCodeGenerator.generateQrImage(loginUrl, 300, 300);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);
            String base64 = Base64Utils.encodeToString(baos.toByteArray());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "qrImageBase64", base64,
                    "loginUrl", loginUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint, который фронтенд на втором устройстве вызывает для подтверждения логина.
     * Ожидает JSON с token, либо в запросе param token.
     * Возвращает JWT.
     */
    @GetMapping("/confirm")
    public void confirmLogin(@RequestParam String token,
                             HttpSession session,
                             HttpServletResponse response) throws IOException {
        QrLoginTokenEntity tokenEntity = qrLoginTokenService.validateToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
        UserEntity user = qrLoginTokenService.findUserByToken(tokenEntity.getToken());

        qrLoginTokenService.markTokenUsed(tokenEntity);

        // Устанавливаем пользователя в сессию
        session.setAttribute("currentUser", user);

        // Перенаправляем на главную или другую защищённую страницу
        response.sendRedirect("/index");
    }
}