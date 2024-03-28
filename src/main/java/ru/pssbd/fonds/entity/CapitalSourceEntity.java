package ru.pssbd.fonds.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "capital_sources")
@Data
public class CapitalSourceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "sum")
    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "id_currency_type")
    private CurrencyTypeEntity currencyType;

    @Column(name = "phone")
    private String phone;

    @Column(name = "donation_date")
    private LocalDate donationDate;

    @ManyToMany
    @JoinTable(name = "capital_sources_donation_types",
            joinColumns = @JoinColumn(name = "id_capital_source"),
            inverseJoinColumns = @JoinColumn(name = "id_donation_type"))
    private List<DonationTypeEntity> donationType;

    public CapitalSourceEntity(BigDecimal sum, CurrencyTypeEntity currencyType, String phone, LocalDate donationDate) {
        this.sum = sum;
        this.currencyType = currencyType;
        this.phone = phone;
        this.donationDate = donationDate;
    }
}
