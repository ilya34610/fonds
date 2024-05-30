package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CapitalSourceInput {

    @NotNull
    private BigDecimal sum;

    @NotNull
    private Short currencyType;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate donationDate;

    @NotNull
    private Integer user;

    @NotNull
    private List<Integer> fonds;

}
