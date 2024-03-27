package ru.pssbd.fonds.dto.input;

import lombok.Data;
import ru.pssbd.fonds.entity.CurrencyTypeEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class CapitalSourceInput {
    private BigInteger id;
    private BigDecimal sum;
    private CurrencyTypeEntity currencyType;
    private String phone;
    private LocalDate donation_date;
}
