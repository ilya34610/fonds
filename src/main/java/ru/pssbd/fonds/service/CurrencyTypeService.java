package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.mappers.CurrencyTypeMapper;
import ru.pssbd.fonds.repository.CurrencyTypeRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CurrencyTypeService {

    private final CurrencyTypeRepository repository;
    private final CurrencyTypeMapper mapper;

    public CurrencyTypeService(CurrencyTypeRepository repository, CurrencyTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CurrencyTypeOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CurrencyTypeOutput getElemById(int id) {
        return repository.findById(BigInteger.valueOf(id))
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

}
