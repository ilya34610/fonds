package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FondOutput {
    private Integer id;
    private String name;
    private CityOutput city;
    private LocalDate creationDate;
    private String phone;
    private UserOutput user;
    private BigDecimal sum;
}
