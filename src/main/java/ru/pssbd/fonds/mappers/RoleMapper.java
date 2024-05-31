package ru.pssbd.fonds.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pssbd.fonds.dto.input.RoleInput;
import ru.pssbd.fonds.dto.output.RoleOutput;
import ru.pssbd.fonds.entity.RoleEntity;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    public RoleEntity fromInput(RoleInput input, RoleEntity entity) {
        entity.setName(input.getName());
        return entity;
    }

    public RoleOutput toOutput(RoleEntity entity) {
        return new RoleOutput(entity.getId(),
                entity.getName()
        );
    }

    public RoleEntity fromInput(RoleInput input) {
        return fromInput(input, new RoleEntity());
    }

}
