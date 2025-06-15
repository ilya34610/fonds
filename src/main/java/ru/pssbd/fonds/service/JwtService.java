package ru.pssbd.fonds.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String jwtSecret; // строка достаточной длины, например base64 или просто случайная

    @Value("${security.jwt.expiration-seconds:86400}")
    private long jwtExpirationSeconds;

    private Key key;

    @PostConstruct
    public void init() {
        // Проверяем длину секрета
        if (jwtSecret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }

        // Используем безопасное преобразование строки в ключ
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Integer userId) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiry = Date.from(now.plusSeconds(jwtExpirationSeconds));
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                // Можно добавить клеймы, например роли: .claim("roles", List<String>...)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token).getBody();
        String subject = claims.getSubject();
        return Long.valueOf(subject);
    }
}