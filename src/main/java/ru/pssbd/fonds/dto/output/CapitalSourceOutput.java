package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CapitalSourceOutput {
    private BigInteger id;
    private BigDecimal sum;
    private CurrencyTypeOutput currencyType;
    private LocalDate donationDate;
    private UserOutput user;
}
