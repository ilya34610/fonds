package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
public class DonationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "sum")
    private BigDecimal sum;

    public DonationEntity(LocalDate date, BigDecimal sum) {
        this.date = date;
        this.sum = sum;
    }
}
