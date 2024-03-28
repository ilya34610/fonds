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
        return new DonationTypeOutput(entity.getId(), entity.getName());
    }

    public DonationTypeEntity fromInput(DonationTypeInput input) {
        return new DonationTypeEntity(input.getName());
    }
}
