package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CitizenInput;
import ru.pssbd.fonds.dto.output.CitizenOutput;
import ru.pssbd.fonds.entity.CitizenEntity;

@Service
public class CitizenMapper {

    public CitizenEntity fromInput(CitizenInput input, CitizenEntity entity) {
        entity.setFio(input.getFio());
        entity.setCity(input.getCity());
        entity.setBirth_date(input.getBirthDate());
        return entity;
    }

    public CitizenOutput toOutput(CitizenEntity entity) {
        return new CitizenOutput(entity.getId(), entity.getFio(), entity.getCity(), entity.getBirth_date());
    }

    public CitizenEntity fromInput(CitizenInput input) {
        return new CitizenEntity(input.getFio(), input.getCity(), input.getBirthDate());
    }

}
