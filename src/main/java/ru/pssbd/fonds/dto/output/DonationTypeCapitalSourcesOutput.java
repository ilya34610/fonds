package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DonationTypeCapitalSourcesOutput {
    DonationTypeOutput donationType;

    CapitalSourceOutput capitalSource;
}
