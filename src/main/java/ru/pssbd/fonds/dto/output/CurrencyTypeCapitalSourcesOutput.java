package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyTypeCapitalSourcesOutput {
    CurrencyTypeOutput currencyType;

    CapitalSourceOutput capitalSource;
}
