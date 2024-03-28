package ru.pssbd.fonds.mappers;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.FondInput;
import ru.pssbd.fonds.dto.output.FondOutput;
import ru.pssbd.fonds.entity.FondEntity;

@Service
public class FondMapper {

    public FondEntity fromInput(FondInput input, FondEntity entity) {
        entity.setName(input.getName());
        entity.setCity(input.getCity());
        entity.setCreationDate(input.getCreationDate());
        entity.setPhone(input.getPhone());
        return entity;
    }

    public FondOutput toOutput(FondEntity entity) {
        return new FondOutput(entity.getId(), entity.getName(), entity.getCity(), entity.getCreationDate(), entity.getPhone());
    }

    public FondEntity fromInput(FondInput input) {
        return new FondEntity(input.getName(), input.getCity(), input.getCreationDate(), input.getPhone());
    }

}
