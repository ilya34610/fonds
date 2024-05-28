package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "capital_sources")
@Data
@NoArgsConstructor
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

    @Column(name = "donation_date")
    private LocalDate donationDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @ManyToMany
    @JoinTable(name = "capital_sources_donation_types",
            joinColumns = @JoinColumn(name = "id_capital_source"),
            inverseJoinColumns = @JoinColumn(name = "id_donation_type"))
    private List<DonationTypeEntity> donationTypes;

    @ManyToMany
    @JoinTable(name = "capital_sources_receipts",
            joinColumns = @JoinColumn(name = "id_capital_source"),
            inverseJoinColumns = @JoinColumn(name = "id_receipt"))
    private List<ReceiptEntity> receipts;

    @ManyToMany
    @JoinTable(name = "capital_sources_fonds",
            joinColumns = @JoinColumn(name = "id_capital_source"),
            inverseJoinColumns = @JoinColumn(name = "id_fond"))
    private List<FondEntity> fonds;

    public CapitalSourceEntity(BigDecimal sum, CurrencyTypeEntity currencyType, LocalDate donationDate) {
        this.sum = sum;
        this.currencyType = currencyType;

        this.donationDate = donationDate;
    }
}
