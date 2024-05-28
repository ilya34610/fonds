package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.FondOutput;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.mappers.FondMapper;
import ru.pssbd.fonds.repository.FondRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FondService {

    private final FondRepository repository;
    private final FondMapper mapper;

    public FondService(FondRepository repository, FondMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<FondOutput> getAllElem() {
        return repository.findAll().stream()
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

}
