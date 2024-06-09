package ru.pssbd.fonds.dto.input;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class FondCitizenInput {

    private Integer fondIds;
    private BigInteger citizenIds;
    private BigDecimal sum;

}
