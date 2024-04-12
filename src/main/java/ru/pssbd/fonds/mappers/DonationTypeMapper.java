package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.DonationTypeInput;
import ru.pssbd.fonds.dto.output.DonationTypeOutput;
import ru.pssbd.fonds.entity.DonationTypeEntity;

@Service
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
        return new DonationTypeEntity(input.getName());
    }
}
