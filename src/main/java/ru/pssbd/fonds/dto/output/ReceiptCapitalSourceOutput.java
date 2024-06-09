package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiptCapitalSourceOutput {

    private ReceiptOutput receipt;

    private CapitalSourceOutput capitalSource;

}
