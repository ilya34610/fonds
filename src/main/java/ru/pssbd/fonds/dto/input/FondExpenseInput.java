package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
public class FondExpenseInput {

    @NotNull
    private BigDecimal sum;

    @NotNull
    private BigInteger citizen;

}
