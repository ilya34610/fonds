package ru.pssbd.fonds.dto.input;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ReceiptInput {

    @NotNull
    private BigDecimal sum;

    @NotNull
    private Integer fond;

}
