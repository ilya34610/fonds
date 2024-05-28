package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.ReceiptInput;
import ru.pssbd.fonds.dto.output.ReceiptOutput;
import ru.pssbd.fonds.entity.ReceiptEntity;
import ru.pssbd.fonds.mappers.ReceiptMapper;
import ru.pssbd.fonds.repository.ReceiptRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    private final ReceiptRepository repository;
    private final ReceiptMapper mapper;

    public ReceiptService(ReceiptRepository repository, ReceiptMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ReceiptOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

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


}
