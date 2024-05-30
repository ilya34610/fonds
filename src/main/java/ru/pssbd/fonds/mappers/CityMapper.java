package ru.pssbd.fonds.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pssbd.fonds.dto.input.CityInput;
import ru.pssbd.fonds.dto.output.CityOutput;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.entity.CityEntity;
import ru.pssbd.fonds.entity.CountryEntity;
import ru.pssbd.fonds.service.CountryService;

@Component
@RequiredArgsConstructor
public class CityMapper {

    private final CountryService countryService;

    private final CountryMapper countryMapper;

    public CityEntity fromInput(CityInput input, CityEntity entity) {
        entity.setName(input.getName());
        entity.setCountry(countryService.get(input.getCountry()));
        return entity;
    }

    public CityOutput toOutput(CityEntity entity) {
        CityOutput output = new CityOutput();
        output.setId(entity.getId());
        output.setName(entity.getName());
        output.setCountry(countryMapper.toOutput(entity.getCountry()));
        return output;
    }

    public CityEntity fromInput(CityInput input) {
        return fromInput(input, new CityEntity());
    }

    public CountryOutput toOutput(CountryEntity entity) {
        return countryMapper.toOutput(entity);
    }


}
