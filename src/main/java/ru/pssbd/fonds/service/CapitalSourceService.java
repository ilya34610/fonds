package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.CapitalSourceOutput;
import ru.pssbd.fonds.mappers.CapitalSourceMapper;
import ru.pssbd.fonds.repository.CapitalSourceRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CapitalSourceService {

    private final CapitalSourceRepository repository;
    private final CapitalSourceMapper mapper;

    public CapitalSourceService(CapitalSourceRepository repository, CapitalSourceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CapitalSourceOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CapitalSourceOutput getElemById(int id) {
        return repository.findById(BigInteger.valueOf(id))
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

}
