package ru.pssbd.fonds.dto.input;

import lombok.Data;
import ru.pssbd.fonds.entity.CountryEntity;

import java.math.BigInteger;

@Data
public class CityInput {
    private BigInteger id;
    private String name;
    private CountryEntity country;
}
