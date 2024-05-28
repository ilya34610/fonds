package ru.pssbd.fonds.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.service.RoleService;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    public UserEntity fromInput(UserInput input, UserEntity entity) {
        entity.setRole(roleService.get(input.getRole()));
        entity.setFio(input.getFio());
        entity.setLogin(input.getLogin());
        entity.setPassword(input.getPassword());
        entity.setPhone(input.getPhone());
        return entity;
    }

    public UserOutput toOutput(UserEntity entity) {
        return new UserOutput(entity.getId(),
                roleMapper.toOutput(entity.getRole()),
                entity.getFio(),
                entity.getLogin(),
                entity.getPassword(),
                entity.getPhone());
    }

    public UserEntity fromInput(UserInput input) {
        return fromInput(input, new UserEntity());
    }

}
