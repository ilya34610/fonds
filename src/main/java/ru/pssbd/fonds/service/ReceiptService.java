package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.ReceiptInput;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.dto.output.ReceiptCapitalSourceOutput;
import ru.pssbd.fonds.dto.output.ReceiptOutput;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.entity.ReceiptEntity;
import ru.pssbd.fonds.mappers.CapitalSourceMapper;
import ru.pssbd.fonds.mappers.ReceiptMapper;
import ru.pssbd.fonds.repository.ReceiptRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReceiptService {

    private final ReceiptRepository repository;
    private final ReceiptMapper mapper;
    private final CapitalSourceMapper capitalSourceMapper;

    public ReceiptService(ReceiptRepository repository, ReceiptMapper mapper, CapitalSourceMapper capitalSourceMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.capitalSourceMapper = capitalSourceMapper;
    }

//    public List<ReceiptOutput> getAllElem() {
//        return repository.findAll().stream()
//                .map(mapper::toOutput)
//                .collect(Collectors.toList());
//    }

    public ReceiptOutput getElemById(BigInteger id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public ReceiptEntity get(BigInteger id) {
        return repository.getById(id);
    }

    public ReceiptEntity save(ReceiptEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(BigInteger id) {
        repository.deleteById(id);
    }

    public void putById(BigInteger id, ReceiptInput input) {
        ReceiptEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }


    public List<ReceiptCapitalSourceOutput> getAllElem() {

        List<ReceiptCapitalSourceOutput> result = new ArrayList<>();

        List<Object[]> data = repository.getAll();
        if (!data.isEmpty()) {

            for (Object[] row : data) {

                ReceiptOutput receipt = mapper.toOutput((ReceiptEntity) row[0]);
                CapitalSourceOutput capitalSource = capitalSourceMapper.toOutput((CapitalSourceEntity) row[1]);
                result.add(new ReceiptCapitalSourceOutput(receipt, capitalSource));
            }
            return result;
        } else {
            return result;
        }

    }




}
