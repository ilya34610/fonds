package ru.pssbd.fonds.dto.output;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Data
@RequiredArgsConstructor
public class HavingRequestOutput {

    CountryOutput country;

    BigInteger count;

    public HavingRequestOutput(CountryOutput country, BigInteger count) {
        this.country = country;
        this.count = count;
    }
}
