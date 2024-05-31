package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CitizenOutput {
    private BigInteger id;
    private CityOutput city;
    private LocalDate birthDate;
    private UserOutput user;
    private BigDecimal sum;

}
