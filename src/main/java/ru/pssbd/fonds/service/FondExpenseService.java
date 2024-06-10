package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.FondExpenseInput;
import ru.pssbd.fonds.dto.output.FondExpenseOutput;
import ru.pssbd.fonds.dto.output.FondFondExpensesOutput;
import ru.pssbd.fonds.dto.output.FondOutput;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.entity.FondExpenseEntity;
import ru.pssbd.fonds.mappers.FondExpenseMapper;
import ru.pssbd.fonds.mappers.FondMapper;
import ru.pssbd.fonds.repository.FondExpenseRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FondExpenseService {

    private final FondExpenseRepository repository;
    private final FondExpenseMapper mapper;

    private final FondMapper fondMapper;

    public FondExpenseService(FondExpenseRepository repository, FondExpenseMapper mapper, FondMapper fondMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.fondMapper = fondMapper;
    }

//    public List<FondExpenseOutput> getAllElem() {
//        return repository.findAll().stream()
//                .map(mapper::toOutput)
//                .collect(Collectors.toList());
//    }

    public FondExpenseOutput getElemById(BigInteger id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public FondExpenseEntity get(BigInteger id) {
        return repository.getById(id);
    }

    public FondExpenseEntity save(FondExpenseEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(BigInteger id) {
        repository.deleteById(id);
    }

    public void putById(BigInteger id, FondExpenseInput input) {
        FondExpenseEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

    public List<FondFondExpensesOutput> getAllElem() {

        List<FondFondExpensesOutput> result = new ArrayList<>();

        List<Object[]> data = repository.getAll();
        if (!data.isEmpty()) {

            for (Object[] row : data) {

                FondExpenseOutput fondExpense = mapper.toOutput((FondExpenseEntity) row[0]);
                FondOutput fond = fondMapper.toOutput((FondEntity) row[1]);
                result.add(new FondFondExpensesOutput(fond, fondExpense));
            }
            return result;
        } else {
            return result;
        }

    }


}
