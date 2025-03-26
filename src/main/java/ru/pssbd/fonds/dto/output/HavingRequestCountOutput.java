package ru.pssbd.fonds.dto.output;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Data
@RequiredArgsConstructor
public class HavingRequestCountOutput {

    CityOutput city;

    BigInteger count;

    public HavingRequestCountOutput(CityOutput city, BigInteger count) {
        this.city = city;
        this.count = count;
    }

}
