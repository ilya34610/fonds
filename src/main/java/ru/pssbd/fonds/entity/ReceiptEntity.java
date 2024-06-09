package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "receipts")
@Data
@NoArgsConstructor
public class ReceiptEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "sum")
    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "id_fond")
    private FondEntity fond;

    @ManyToMany(mappedBy = "receipts")
    private List<CapitalSourceEntity> capitalSources;

    public ReceiptEntity(BigDecimal sum, FondEntity fond) {

        this.sum = sum;
        this.fond = fond;

    }
}
