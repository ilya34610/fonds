package ru.pssbd.fonds.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.FondInput;
import ru.pssbd.fonds.dto.output.FondOutput;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.service.CityService;
import ru.pssbd.fonds.service.UserService;

@Service
@RequiredArgsConstructor
public class FondMapper {

    private final CityService cityService;
    private final UserService userService;

    private final CityMapper cityMapper;
    private final UserMapper userMapper;

    public FondEntity fromInput(FondInput input, FondEntity entity) {
        entity.setName(input.getName());
        entity.setCity(cityService.get(input.getCity()));
        entity.setCreationDate(input.getCreationDate());
        entity.setPhone(input.getPhone());
        entity.setUser(userService.get(input.getUser()));
        entity.setSum(input.getSum());
        return entity;
    }

    public FondOutput toOutput(FondEntity entity) {
        return new FondOutput(entity.getId(),
                entity.getName(),
                cityMapper.toOutput(entity.getCity()),
                entity.getCreationDate(),
                entity.getPhone(),
                userMapper.toOutput(entity.getUser()),
                entity.getSum()
        );
    }

    public FondEntity fromInput(FondInput input) {
        return fromInput(input, new FondEntity());
    }

}
