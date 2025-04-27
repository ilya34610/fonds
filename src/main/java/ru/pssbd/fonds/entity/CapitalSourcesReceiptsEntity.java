package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "capital_sources_receipts")
@Data
@NoArgsConstructor
public class CapitalSourcesReceiptsEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @MapsId("idCapitalSource")
    @JoinColumn(name = "id_capital_source")
    private CapitalSourceEntity capitalSource;

    @ManyToOne
    @MapsId("idReceipt")
    @JoinColumn(name = "id_receipt")
    private  ReceiptEntity receipt;

}
