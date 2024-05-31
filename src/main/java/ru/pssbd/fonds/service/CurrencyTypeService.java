package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.CurrencyTypeInput;
import ru.pssbd.fonds.dto.output.CurrencyTypeOutput;
import ru.pssbd.fonds.entity.CurrencyTypeEntity;
import ru.pssbd.fonds.mappers.CurrencyTypeMapper;
import ru.pssbd.fonds.repository.CurrencyTypeRepository;

import javax.persistence.EntityNotFoundException;
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
        return repository.findById((short) id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public CurrencyTypeEntity get(short id) {
        return repository.getById(id);
    }

    public CurrencyTypeEntity save(CurrencyTypeEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(Short id) {
        repository.deleteById(id);
    }

    public void putById(Short id, CurrencyTypeInput input) {
        CurrencyTypeEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

}
