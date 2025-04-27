package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pssbd.fonds.dto.input.DonationTypeInput;
import ru.pssbd.fonds.dto.input.FondInput;
import ru.pssbd.fonds.dto.output.*;
import ru.pssbd.fonds.entity.*;
import ru.pssbd.fonds.mappers.FondExpenseMapper;
import ru.pssbd.fonds.mappers.FondMapper;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.repository.CitizensFondsRepository;
import ru.pssbd.fonds.repository.FondRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FondService {

    private final UserService userService;
    private final UserMapper userMapper;

    private final FondRepository repository;
    private final FondMapper mapper;

    private final CityService cityService;
    private final FondExpenseMapper fondExpenseMapper;

    public FondService(UserService userService, CitizensFondsRepository citizensFondsRepository, UserMapper userMapper, FondRepository repository, FondMapper mapper, CityService cityService, FondExpenseMapper fondExpenseMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.repository = repository;
        this.mapper = mapper;
        this.cityService = cityService;
        this.fondExpenseMapper = fondExpenseMapper;
    }

    public List<FondOutput> getAllElem() {
        return (List<FondOutput>) repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public FondOutput getElemById(BigInteger id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public FondEntity get(BigInteger id) {
        return repository.getById(id);
    }

//    public List<FondCapitalSourceOutput> getAllFondsForCurrentDonater(UserOutput donater) {
//        List<FondCapitalSourceOutput> result = new ArrayList<>();
//        UserEntity userEntity = userService.get(donater.getId());
//
//        List<FondOutput> data = repository.findAllFondsByUser(userEntity);
//        if (!data.isEmpty()) {
//
////            for (Object[] row : data) {
////                FondEntity fond = (FondEntity) row[0];
////                CapitalSourceEntity capitalSource = (CapitalSourceEntity) row[1];
////                result.add(new FondCapitalSourceOutput(fond, capitalSource));
////            }
//            result.add()
//            return result;
//        } else {
//            return result;
//        }
//
//    }

    public List<FondOutput> getAllElemForCurrentDonater(UserOutput donater) {
        UserEntity userEntity = userService.get(donater.getId());
        List<FondEntity> fondEntities = repository.findAllElemByCurrentStaff(userEntity);
        return fondEntities.stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }


    public FondEntity save(FondEntity entity) {
        return repository.save(entity);
    }


    @Transactional
    public void minusBalance(BigDecimal sum, BigInteger id) {
        repository.minusBalance(sum, id);

    }

    public List<FondFondExpensesOutput> getFondsByUser(Integer idCitizen) {
        List<FondFondExpensesOutput> result = new ArrayList<>();
        List<Object[]> data = repository.findAllElemByCurrentCitizen(idCitizen);
        if (!data.isEmpty()) {

            for (Object[] row : data) {
                FondEntity fond = (FondEntity) row[0];
                FondExpenseEntity fondExpense = (FondExpenseEntity) row[1];
                result.add(new FondFondExpensesOutput(mapper.toOutput(fond), fondExpenseMapper.toOutput(fondExpense)));
            }
            return result;
        } else {
            return result;
        }

//        List<FondFondExpensesOutput> dtos = new ArrayList<>();
//
//
//        for (Object[] result : results) {
//            FondEntity fond = new FondEntity();
//            fond.setId((Integer) result[0]);
//            fond.setName((String) result[1]);
//            fond.setCity(new CityEntity((BigInteger) result[2])); // Assuming CityEntity has a constructor with id
//            fond.setCreationDate((LocalDate) result[3]);
//            fond.setPhone((String) result[4]);
//            fond.setUser(new UserEntity((Integer) result[5])); // Assuming UserEntity has a constructor with id
//            fond.setSum((BigDecimal) result[6]);
//
//            FondExpenseEntity fondExpense = new FondExpenseEntity();
//            fondExpense.setId((BigInteger) result[7]);
//            fondExpense.setSum((BigDecimal) result[8]);
//            fondExpense.setCitizen(new CitizenEntity((BigInteger) result[9])); // Assuming CitizenEntity has a constructor with id
//
//            FondFondExpensesOutput dto = new FondFondExpensesOutput(fond, fondExpense); // Assuming you have a DTO to hold both entities
//            dtos.add(dto);
//        }
//
//
//        return dtos;
    }


    public List<RequestWithCaseOutput> getElemRequestWithCase() {
        List<RequestWithCaseOutput> result = new ArrayList<>();

        List<Object[]> data = repository.getElemRequestWithCase();
        if (!data.isEmpty()) {
            for (Object[] row : data) {

                FondOutput fond = new FondOutput();
                fond.setName(row[1].toString());

                BigInteger bigIntegerValue = (BigInteger) row[2];
                Integer integerValue = bigIntegerValue.intValue();
                fond.setCity(cityService.getElemById(integerValue));

                fond.setPhone((String) row[4]);
                fond.setSum((BigDecimal) row[6]);
                result.add(new RequestWithCaseOutput(fond, (String) row[7]));
            }
            return result;
        } else {
            return result;
        }

    }


    public List<FondOutput> getElemQueryNotIn() {
        List<FondOutput> result = new ArrayList<>();

        List<Object[]> data = repository.getElemQueryNotIn();
        if (!data.isEmpty()) {
            for (Object[] row : data) {

                FondOutput fond = new FondOutput();
                fond.setId((BigInteger) row[0]);

                fond.setName(row[1].toString());

                BigInteger bigIntegerValue = (BigInteger) row[2];
                Integer integerValue = bigIntegerValue.intValue();
                fond.setCity(cityService.getElemById(integerValue));
                fond.setCreationDate(((java.sql.Date) row[3]).toLocalDate());
                fond.setPhone((String) row[4]);
                fond.setUser(userService.getElemById((Integer) row[5]));
                fond.setSum((BigDecimal) row[6]);
                result.add(fond);
            }
            return result;
        } else {
            return null;
        }

    }

    public void putById(BigInteger id, FondInput input) {
        FondEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

    public void deleteById(BigInteger id) {
        repository.deleteById(id);
    }

}
