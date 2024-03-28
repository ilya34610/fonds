package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pssbd.fonds.entity.CityEntity;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CitizenOutput {
    private BigInteger id;
    private String fio;
    private CityEntity city;
    private LocalDate birthDate;
}
