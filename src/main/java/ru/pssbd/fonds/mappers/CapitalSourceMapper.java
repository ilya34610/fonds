package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.entity.CapitalSourceEntity;

@Service
public class CapitalSourceMapper {

    public CapitalSourceEntity fromInput(CapitalSourceInput input, CapitalSourceEntity entity) {
        entity.setSum((input.getSum()));
        entity.setCurrencyType(input.getCurrencyType());
        entity.setPhone(input.getPhone());
        entity.setDonationDate(input.getDonationDate());
        return entity;
    }

    public CapitalSourceOutput toOutput(CapitalSourceEntity entity) {
        return new CapitalSourceOutput(entity.getId(), entity.getSum(), entity.getCurrencyType(), entity.getPhone(), entity.getDonationDate());
    }

    public CapitalSourceEntity fromInput(CapitalSourceInput input) {
        return new CapitalSourceEntity(input.getSum(), input.getCurrencyType(), input.getPhone(), input.getDonationDate());
    }
}
