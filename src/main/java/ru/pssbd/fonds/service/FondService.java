package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pssbd.fonds.dto.output.FondCapitalSourceOutput;
import ru.pssbd.fonds.dto.output.FondFondExpensesOutput;
import ru.pssbd.fonds.dto.output.FondOutput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.entity.FondExpenseEntity;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.mappers.FondExpenseMapper;
import ru.pssbd.fonds.mappers.FondMapper;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.repository.CitizensFondsRepository;
import ru.pssbd.fonds.repository.FondRepository;

import java.math.BigDecimal;
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

    private final FondExpenseMapper fondExpenseMapper;

    public FondService(UserService userService, CitizensFondsRepository citizensFondsRepository, UserMapper userMapper, FondRepository repository, FondMapper mapper, FondExpenseMapper fondExpenseMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.repository = repository;
        this.mapper = mapper;
        this.fondExpenseMapper = fondExpenseMapper;
    }

    public List<FondOutput> getAllElem() {
        return (List<FondOutput>) repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public FondOutput getElemById(int id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public FondEntity get(int id) {
        return repository.getById(id);
    }

    public List<FondCapitalSourceOutput> getAllFondsForCurrentDonater(UserOutput donater) {
        List<FondCapitalSourceOutput> result = new ArrayList<>();
        UserEntity userEntity = userService.get(donater.getId());

        List<Object[]> data = repository.findAllFondsByUser(userEntity);
        if (!data.isEmpty()) {

            for (Object[] row : data) {
                FondEntity fond = (FondEntity) row[0];
                CapitalSourceEntity capitalSource = (CapitalSourceEntity) row[1];
                result.add(new FondCapitalSourceOutput(fond, capitalSource));
            }
            return result;
        } else {
            return result;
        }

    }

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
    public void minusBalance(BigDecimal sum, Integer id) {
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




}
