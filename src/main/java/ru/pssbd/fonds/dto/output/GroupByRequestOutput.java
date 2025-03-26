package ru.pssbd.fonds.dto.output;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class GroupByRequestOutput {

    BigDecimal cum;

    String currencyType;


    public GroupByRequestOutput(BigDecimal cum, String currencyType) {
        this.cum = cum;
        this.currencyType = currencyType;
    }
}
