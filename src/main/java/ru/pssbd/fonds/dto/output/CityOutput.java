package ru.pssbd.fonds.dto.output;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CityOutput {
    private BigInteger id;
    private String name;
    private CountryOutput country;
}
