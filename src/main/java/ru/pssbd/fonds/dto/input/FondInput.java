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
public class FondInput {

    @NotNull
    private String name;

    @NotNull
    private BigInteger city;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    @NotNull
    private String phone;

    @NotNull
    private Integer user;

    @NotNull
    private BigDecimal sum;

}
