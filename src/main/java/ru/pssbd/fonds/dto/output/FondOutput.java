package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pssbd.fonds.entity.CityEntity;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FondOutput {
    private Integer id;
    private CityEntity city;
    private LocalDate creation_date;
    private String phone;
}