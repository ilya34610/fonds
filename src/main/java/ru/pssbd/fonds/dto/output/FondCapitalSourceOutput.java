package ru.pssbd.fonds.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.entity.FondEntity;

@Data
@AllArgsConstructor
public class FondCapitalSourceOutput {

    private FondEntity fond;

    private CapitalSourceEntity capitalSource;

}
