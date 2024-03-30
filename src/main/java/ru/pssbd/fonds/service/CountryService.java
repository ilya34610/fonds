package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.mappers.CountryMapper;
import ru.pssbd.fonds.repository.CountryRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository repository;
    private final CountryMapper mapper;

    public CountryService(CountryRepository repository, CountryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CountryOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CountryOutput getElemById(int id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

}
