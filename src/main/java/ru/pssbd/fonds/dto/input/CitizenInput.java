package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CitizenInput {

    @NotNull
    private BigInteger city;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @NotNull
    private Integer user;

    @NotNull
    private BigDecimal sum;

}
