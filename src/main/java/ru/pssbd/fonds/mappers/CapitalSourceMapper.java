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
import ru.pssbd.fonds.service.DonationTypeService;
import ru.pssbd.fonds.service.FondService;
import ru.pssbd.fonds.service.UserService;

import java.math.BigInteger;
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
    private final DonationTypeService donationTypeService;

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

        BigInteger fondsId = new BigInteger(input.getFonds().get(0).toString());
        FondEntity fondTemp = fondRepository.getById(fondsId);
//        List<FondEntity> fondList = new ArrayList<>();
//        fondList.add(fondTemp);
        entity.setFond(fondTemp);



        List<DonationTypeEntity> donationTypeList = new ArrayList<>();
        for (Short donationTypeId : input.getDonationTypes()) {

            DonationTypeEntity donationTypeTemp = donationTypeRepository.getById(donationTypeId);

            donationTypeList.add(donationTypeTemp);
            entity.setDonationTypes(donationTypeList);

        }



        return entity;
    }

    public CapitalSourceOutput toOutput(@NotNull CapitalSourceEntity entity) {

        return new CapitalSourceOutput(entity.getId(),
                entity.getSum(),
                currencyTypeMapper.toOutput(entity.getCurrencyType()),
                entity.getDonationDate(),
                userMapper.toOutput(entity.getUser()),
                fondMapper.toOutput(entity.getFond()),
                entity.getDonationTypes().stream()
                        .map(DonationTypeEntity::getId)
                        .collect(Collectors.toList()));
    }

}
//(DonationTypeEntity id) -> donationTypeService.getElemById(id).getId()
//entity.getDonationTypes()
//                        .stream()
//                        .map((DonationTypeEntity id) -> donationTypeService.getElemById(id).getId())
//        .collect(Collectors.toList()));