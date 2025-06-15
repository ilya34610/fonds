package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pssbd.fonds.entity.QrLoginTokenEntity;
import ru.pssbd.fonds.entity.UserEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface QrLoginTokenRepository extends JpaRepository<QrLoginTokenEntity, Long> {

    Optional<QrLoginTokenEntity> findByToken(String token);

    void deleteByExpiresAtBefore(LocalDate now);

    @Query("SELECT t.user FROM QrLoginTokenEntity t WHERE t.token = :token")
    Optional<UserEntity> findUserByToken(@Param("token") String token);
}
