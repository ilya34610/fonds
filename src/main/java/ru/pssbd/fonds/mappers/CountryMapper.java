package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Component;
import ru.pssbd.fonds.dto.input.CountryInput;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.entity.CountryEntity;

@Component
public class CountryMapper {
    public CountryEntity fromInput(CountryInput input, CountryEntity entity) {
        entity.setName(input.getName());
        return entity;
    }

    public CountryOutput toOutput(CountryEntity entity) {
        CountryOutput output = new CountryOutput();
        output.setId(entity.getId());
        output.setName(entity.getName());
        return output;
    }

    public CountryEntity fromInput(CountryInput input) {
        return fromInput(input, new CountryEntity());
    }
}
