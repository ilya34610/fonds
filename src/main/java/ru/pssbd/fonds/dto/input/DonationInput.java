package ru.pssbd.fonds.dto.input;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class DonationInput {
    private BigInteger id;
    private LocalDate date;
    private BigDecimal sum;
}
