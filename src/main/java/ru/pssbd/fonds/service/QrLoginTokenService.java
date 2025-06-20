package ru.pssbd.fonds.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.entity.QrLoginTokenEntity;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.repository.QrLoginTokenRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service

public class QrLoginTokenService {
    private final QrLoginTokenRepository repository;
    private final long tokenTtlSeconds;

    public QrLoginTokenService(QrLoginTokenRepository repository,
                               @Value("${security.qr.token-ttl-seconds:300}") long tokenTtlSeconds) {
        this.repository = repository;
        this.tokenTtlSeconds = tokenTtlSeconds;
    }

    @Transactional
    public QrLoginTokenEntity createTokenForUser(UserEntity user) {
        QrLoginTokenEntity tokenEntity = new QrLoginTokenEntity();
        tokenEntity.setToken(UUID.randomUUID().toString().replace("-", "")); // без дефисов, длина ~32
        tokenEntity.setUser(user);
        Instant now = Instant.now();
        tokenEntity.setCreatedAt(now);
        tokenEntity.setExpiresAt(now.plusSeconds(tokenTtlSeconds));
        tokenEntity.setUsed(false);
        return repository.save(tokenEntity);
    }

    @Transactional
    public Optional<QrLoginTokenEntity> validateToken(String tokenValue) {
        return repository.findByToken(tokenValue)
                .filter(t -> !t.isUsed())
                .filter(t -> Instant.now().isBefore(t.getExpiresAt()));
    }

    @Transactional
    public void markTokenUsed(QrLoginTokenEntity token) {
        token.setUsed(true);
        repository.save(token);
    }

    @Transactional
    public void cleanupExpired() {
        repository.deleteByExpiresAtBefore(LocalDate.from(Instant.now()));
    }

    @Transactional
    public UserEntity findUserByToken(String token) {
        return repository.findUserByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("User not found for token: " + token));
    }
}
