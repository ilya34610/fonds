package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapitalSourceOutput {
    private BigInteger id;
    private BigDecimal sum;
    private CurrencyTypeOutput currencyType;
    private LocalDate donationDate;
    private UserOutput user;
    private FondOutput fond;
    private List<Short> donationTypeIds;
}
