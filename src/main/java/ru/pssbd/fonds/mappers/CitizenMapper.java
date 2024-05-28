package ru.pssbd.fonds.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CitizenInput;
import ru.pssbd.fonds.dto.output.CitizenOutput;
import ru.pssbd.fonds.entity.CitizenEntity;
import ru.pssbd.fonds.service.CityService;
import ru.pssbd.fonds.service.UserService;

@Service
@RequiredArgsConstructor
public class CitizenMapper {

    private final CityService cityService;
    private final UserService userService;

    private final CityMapper cityMapper;
    private final UserMapper userMapper;

    public CitizenEntity fromInput(CitizenInput input, CitizenEntity entity) {
        entity.setCity(cityService.get(input.getCity()));
        entity.setBirthDate(input.getBirthDate());
        entity.setUser(userService.get(input.getUser()));
        entity.setSum(input.getSum());
        return entity;
    }

    public CitizenOutput toOutput(CitizenEntity entity) {
        return new CitizenOutput(entity.getId(),
                cityMapper.toOutput(entity.getCity()),
                entity.getBirthDate(),
                userMapper.toOutput(entity.getUser()),
                entity.getSum()
        );
    }

    public CitizenEntity fromInput(CitizenInput input) {
        return fromInput(input, new CitizenEntity());
    }

}
