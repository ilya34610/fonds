package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CurrencyTypeInput;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.entity.CurrencyTypeEntity;

@Service
public class CurrencyTypeMapper {

    public CurrencyTypeEntity fromInput(CurrencyTypeInput input, CurrencyTypeEntity entity) {
        entity.setName(input.getName());
        return entity;
    }

    public CurrencyTypeOutput toOutput(CurrencyTypeEntity entity) {
        return new CurrencyTypeOutput(entity.getId(), entity.getName());
    }

    public CurrencyTypeEntity fromInput(CurrencyTypeInput input) {
        return new CurrencyTypeEntity(input.getName());
    }

}
