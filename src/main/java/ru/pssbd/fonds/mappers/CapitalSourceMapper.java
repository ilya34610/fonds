package ru.pssbd.fonds.mappers;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.service.CurrencyTypeService;
import ru.pssbd.fonds.service.UserService;

@Service
@RequiredArgsConstructor
public class CapitalSourceMapper {

    private final CurrencyTypeService currencyTypeService;
    private final UserService userService;

    private final CurrencyTypeMapper currencyTypeMapper;
    private final UserMapper userMapper;


    public CapitalSourceEntity fromInput(@NotNull CapitalSourceInput input) {
        return fromInput(input, new CapitalSourceEntity());
    }

    public CapitalSourceEntity fromInput(CapitalSourceInput input, CapitalSourceEntity entity) {
        entity.setSum(input.getSum());
        entity.setCurrencyType(currencyTypeService.get(input.getCurrencyType()));
        entity.setDonationDate(input.getDonationDate());
        entity.setUser(userService.get(input.getUser()));
        return entity;
    }

    public CapitalSourceOutput toOutput(@NotNull CapitalSourceEntity entity) {
        return new CapitalSourceOutput(entity.getId(),
                entity.getSum(),
                currencyTypeMapper.toOutput(entity.getCurrencyType()),
                entity.getDonationDate(),
                userMapper.toOutput(entity.getUser()));
    }

}
