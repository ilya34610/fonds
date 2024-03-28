package ru.pssbd.fonds.dto.input;

import lombok.Data;
import ru.pssbd.fonds.entity.CityEntity;

import java.time.LocalDate;

@Data
public class FondInput {
    private Integer id;
    private String name;
    private CityEntity city;
    private LocalDate creationDate;
    private String phone;
}
