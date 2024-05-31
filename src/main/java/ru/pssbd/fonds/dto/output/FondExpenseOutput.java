package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
public class FondExpenseOutput {
    private BigInteger id;
    private BigDecimal sum;
    private CitizenOutput citizen;
}
