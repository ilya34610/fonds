package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CityInput;
import ru.pssbd.fonds.dto.output.CityOutput;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.entity.CityEntity;
import ru.pssbd.fonds.mappers.CityMapper;
import ru.pssbd.fonds.repository.CityRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository repository;
    private final CityMapper mapper;

    public CityService(CityRepository repository, CityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CityOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CityOutput getElemById(int id) {
        return repository.findById(BigInteger.valueOf(id))
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public CityEntity save(CityEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(BigInteger id) {
        repository.deleteById(id);
    }

    public void putById(BigInteger id, CityInput input) {
        CityEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

    public List<CountryOutput> getAllCountries() {
        return repository.findAllCountries().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());

    }
}
