package ru.pssbd.fonds.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CountryInput;
import ru.pssbd.fonds.dto.output.CountryOutput;
import ru.pssbd.fonds.entity.CountryEntity;
import ru.pssbd.fonds.mappers.CountryMapper;
import ru.pssbd.fonds.repository.CountryRepository;

import javax.persistence.EntityNotFoundException;
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
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public CountryOutput getElemById(int id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public CountryEntity get(int id) {
        return repository.getById(id);

    }

    public CountryEntity save(CountryEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public void putById(int id, CountryInput input) {
        CountryEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

}
