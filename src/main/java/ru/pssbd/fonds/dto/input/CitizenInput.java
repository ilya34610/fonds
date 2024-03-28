package ru.pssbd.fonds.dto.input;

import lombok.Data;
import ru.pssbd.fonds.entity.CityEntity;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class CitizenInput {
    private BigInteger id;
    private String fio;
    private CityEntity city;
    private LocalDate birthDate;
}
