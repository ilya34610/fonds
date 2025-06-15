package ru.pssbd.fonds.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.awt.geom.AffineTransform;

@Controller
public class CaptchaEndpoint {
    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;
    private static final int LENGTH = 5;
    private final SecureRandom random = new SecureRandom();

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Генерация текста капчи
        String captchaText = generateCaptchaText(LENGTH);

        // Сохранение в сессии
        HttpSession session = request.getSession();
        session.setAttribute("CAPTCHA", captchaText);

        // Подготовка изображения
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Антиалиасинг для текста и фигур
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1) Рисуем фон: случайный светлый цвет
        Color bgColor = new Color(
                200 + random.nextInt(56),
                200 + random.nextInt(56),
                200 + random.nextInt(56)
        );
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // 2) Добавляем шум: линии
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            Color lineColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(lineColor);
            g2d.drawLine(x1, y1, x2, y2);
        }
        // 3) Добавляем шум: точки
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            Color dotColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(dotColor);
            g2d.fillOval(x, y, 2, 2);
        }

        // 4) Рисуем текст капчи по символам с поворотом
        Font font = new Font("Arial", Font.BOLD, 40);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        int charWidth = WIDTH / LENGTH;
        for (int i = 0; i < captchaText.length(); i++) {
            char c = captchaText.charAt(i);
            // Сохраняем текущее преобразование
            AffineTransform orig = g2d.getTransform();

            // Положение символа
            int x = i * charWidth + (charWidth - fm.charWidth(c)) / 2;
            int y = HEIGHT / 2 + fm.getAscent() / 2 - 5;

            // Случайный поворот ±30°
            double angle = (random.nextDouble() - 0.5) * Math.toRadians(30);
            g2d.rotate(angle, x + fm.charWidth(c) / 2.0, y - fm.getAscent() / 2.0);

            // Цвет символа – тёмный диапазон для контраста
            Color textColor = new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150));
            g2d.setColor(textColor);

            g2d.drawString(String.valueOf(c), x, y);

            // Восстанавливаем преобразование
            g2d.setTransform(orig);
        }

        // 5) Освобождаем графику
        g2d.dispose();

        // Устанавливаем заголовки, чтобы не кэшировалось
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Отправляем изображение
        ImageIO.write(image, "png", response.getOutputStream());
    }

    private String generateCaptchaText(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}