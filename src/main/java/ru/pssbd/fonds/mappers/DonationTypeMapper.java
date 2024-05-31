package ru.pssbd.fonds.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pssbd.fonds.dto.input.DonationTypeInput;
import ru.pssbd.fonds.dto.output.DonationTypeOutput;
import ru.pssbd.fonds.entity.DonationTypeEntity;

@Component
@RequiredArgsConstructor
public class DonationTypeMapper {
    public DonationTypeEntity fromInput(DonationTypeInput input, DonationTypeEntity entity) {
        entity.setName(input.getName());
        return entity;
    }

    public DonationTypeOutput toOutput(DonationTypeEntity entity) {
        DonationTypeOutput output = new DonationTypeOutput();
        output.setId(entity.getId());
        output.setName(entity.getName());
        return output;
    }

    public DonationTypeEntity fromInput(DonationTypeInput input) {
        return fromInput(input, new DonationTypeEntity());
    }
}
