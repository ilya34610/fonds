package ru.pssbd.fonds.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "capital_sources")
public class CapitalSourceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "sum")
    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "id_currency_type")
    private CurrencyTypeEntity city;

    @Column(name = "phone")
    private String phone;

    @Column(name = "donation_date")
    private LocalDate donation_date;

    @ManyToMany
    @JoinTable(name = "capital_sources_donation_types",
            joinColumns = @JoinColumn(name = "id_capital_source"),
            inverseJoinColumns = @JoinColumn(name = "id_donation_type"))
    private List<DonationTypeEntity> donationType;

}
