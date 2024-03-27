package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pssbd.fonds.entity.CountryEntity;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class CityOutput {
    private BigInteger id;
    private String phone;
    private CountryEntity country;
}
