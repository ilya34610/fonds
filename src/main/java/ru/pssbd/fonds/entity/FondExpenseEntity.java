package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "fond_expenses")
@Data
@NoArgsConstructor
public class FondExpenseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "sum")
    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "id_citizen")
    private CitizenEntity citizen;

    @ManyToMany(mappedBy = "fondExpenses")
    private List<FondEntity> fonds;

    public FondExpenseEntity(BigDecimal sum, CitizenEntity citizen) {
        this.sum = sum;
        this.citizen = citizen;
    }


}

//@Entity
//@Table(name = "receipts")
//@Data
//@NoArgsConstructor
//public class ReceiptEntity {
//
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private BigInteger id;
//
//    @Column(name = "sum")
//    private BigDecimal sum;
//
//    @ManyToOne
//    @JoinColumn(name = "id_fond")
//    private FondEntity fond;
//
//    @ManyToMany(mappedBy = "receipts")
//    private List<CapitalSourceEntity> capitalSources;
//
//    public ReceiptEntity(BigDecimal sum, FondEntity fond) {
//
//        this.sum = sum;
//        this.fond = fond;
//
//    }
//}
