package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.CitizenOutput;
import ru.pssbd.fonds.mappers.CitizenMapper;
import ru.pssbd.fonds.repository.CitizenRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CitizenService {

    private final CitizenRepository repository;
    private final CitizenMapper mapper;

    public CitizenService(CitizenRepository repository, CitizenMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CitizenOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CitizenOutput getElemById(int id) {
        return repository.findById(BigInteger.valueOf(id))
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

}
