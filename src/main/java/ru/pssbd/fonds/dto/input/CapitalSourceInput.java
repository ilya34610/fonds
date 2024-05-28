package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CapitalSourceInput {

    @NotNull
    private BigDecimal sum;

    @NotNull
    private Short currencyType;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate donationDate;

    @NotNull
    private Integer user;

}
