package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

//    @ManyToMany
//    @JoinTable(
//            name = "parent_child",
//            joinColumns = @JoinColumn(name = "parent_id"),
//            inverseJoinColumns = @JoinColumn(name = "child_id")
//    )
//    private List<CapitalSourceReceiptsEntity> capitalSourceReceipts = new ArrayList<>();

    public ReceiptEntity(BigDecimal sum, FondEntity fond) {

        this.sum = sum;
        this.fond = fond;

    }
}
