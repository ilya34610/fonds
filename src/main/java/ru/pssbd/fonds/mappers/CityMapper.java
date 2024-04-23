package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CityInput;
import ru.pssbd.fonds.dto.output.CityOutput;
import ru.pssbd.fonds.entity.CityEntity;

@Service
public class CityMapper {

    public CityEntity fromInput(CityInput input, CityEntity entity) {
        entity.setName(input.getName());
        entity.setCountry(input.getCountry());
        return entity;
    }

    public CityOutput toOutput(CityEntity entity) {
        CityOutput output = new CityOutput();
        output.setId(entity.getId());
        output.setName(entity.getName());
        output.setCountry(entity.getCountry());
        return output;
    }

    public CityEntity fromInput(CityInput input) {
        return new CityEntity(input.getName(), input.getCountry());
    }

}
