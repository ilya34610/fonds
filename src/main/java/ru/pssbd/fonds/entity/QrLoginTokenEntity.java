package ru.pssbd.fonds.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "qr_login_tokens")
public class QrLoginTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", nullable = false, unique = true, length = 64)
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    @Column(name = "created_at", nullable = false)
    private Instant  createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used", nullable = false)
    private boolean used = false;
}
