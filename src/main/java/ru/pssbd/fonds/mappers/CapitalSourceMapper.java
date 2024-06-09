package ru.pssbd.fonds.mappers;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pssbd.fonds.dto.input.CapitalSourceInput;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.entity.DonationTypeEntity;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.repository.DonationTypeRepository;
import ru.pssbd.fonds.repository.FondRepository;
import ru.pssbd.fonds.service.CurrencyTypeService;
import ru.pssbd.fonds.service.FondService;
import ru.pssbd.fonds.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CapitalSourceMapper {

    @Autowired
    private final CurrencyTypeService currencyTypeService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final FondService fondService;
    @Autowired
    private final CurrencyTypeMapper currencyTypeMapper;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final FondMapper fondMapper;
    private final DonationTypeRepository donationTypeRepository;

    private final DonationTypeMapper donationTypeMapper;
    @Autowired
    private FondRepository fondRepository;


    public CapitalSourceEntity fromInput(@NotNull CapitalSourceInput input) {
        return fromInput(input, new CapitalSourceEntity());
    }

    public CapitalSourceEntity fromInput(CapitalSourceInput input, CapitalSourceEntity entity) {
        entity.setSum(input.getSum());
        entity.setCurrencyType(currencyTypeService.get(input.getCurrencyType()));
        entity.setDonationDate(input.getDonationDate());
        entity.setUser(userService.get(input.getUser()));

        Integer fondsId = input.getFonds().get(0);
        FondEntity fondTemp = fondRepository.getById(fondsId.intValue());
        List<FondEntity> fondList = new ArrayList<>();
        fondList.add(fondTemp);
        entity.setFonds(fondList);

        Integer donationTypeId = input.getDonationTypes().get(0);
        DonationTypeEntity donationTypeTemp = donationTypeRepository.getById(donationTypeId.shortValue());
        List<DonationTypeEntity> donationTypeList = new ArrayList<>();
        donationTypeList.add(donationTypeTemp);
        entity.setDonationTypes(donationTypeList);
        return entity;
    }

    public CapitalSourceOutput toOutput(@NotNull CapitalSourceEntity entity) {
        return new CapitalSourceOutput(entity.getId(),
                entity.getSum(),
                currencyTypeMapper.toOutput(entity.getCurrencyType()),
                entity.getDonationDate(),
                userMapper.toOutput(entity.getUser()),
                entity.getFonds().stream().map(fondMapper::toOutput).collect(Collectors.toList()),
                entity.getDonationTypes().stream().map(donationTypeMapper::toOutput).collect(Collectors.toList()));
    }

}
