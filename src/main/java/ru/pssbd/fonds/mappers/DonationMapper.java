package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.DonationInput;
import ru.pssbd.fonds.dto.output.DonationOutput;
import ru.pssbd.fonds.entity.DonationEntity;

@Service
public class DonationMapper {

    public DonationEntity fromInput(DonationInput input, DonationEntity entity) {
        entity.setSum(input.getSum());
        entity.setDate(input.getDate());
        return entity;
    }

    public DonationOutput toOutput(DonationEntity entity) {
        return new DonationOutput(entity.getId(), entity.getDate(), entity.getSum());
    }

    public DonationEntity fromInput(DonationInput input) {
        return new DonationEntity(input.getDate(), input.getSum());
    }

}
