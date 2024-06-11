package ru.pssbd.fonds.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pssbd.fonds.dto.input.FondExpenseInput;
import ru.pssbd.fonds.dto.output.FondExpenseOutput;
import ru.pssbd.fonds.entity.FondExpenseEntity;
import ru.pssbd.fonds.service.CitizenService;

@Component
public class FondExpenseMapper {

    private final CitizenService citizenService;
    private final CitizenMapper citizenMapper;

    @Autowired
    public FondExpenseMapper(CitizenService citizenService, CitizenMapper citizenMapper) {
        this.citizenService = citizenService;
        this.citizenMapper = citizenMapper;
    }


    public FondExpenseEntity fromInput(FondExpenseInput input, FondExpenseEntity entity) {
        entity.setSum(input.getSum());
        entity.setCitizen(citizenService.get(input.getCitizen()));
        return entity;
    }

    public FondExpenseOutput toOutput(FondExpenseEntity entity) {
        return new FondExpenseOutput(entity.getId(),
                entity.getSum(),
                citizenMapper.toOutput(entity.getCitizen()));
    }

    public FondExpenseEntity fromInput(FondExpenseInput input) {
        return fromInput(input, new FondExpenseEntity());
    }


}
