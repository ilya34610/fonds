package ru.pssbd.fonds.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "donations")
public class DonationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "sum")
    private BigDecimal sum;

}
