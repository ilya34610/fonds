package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.RoleInput;
import ru.pssbd.fonds.dto.output.RoleOutput;
import ru.pssbd.fonds.entity.RoleEntity;
import ru.pssbd.fonds.mappers.RoleMapper;
import ru.pssbd.fonds.repository.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    public RoleService(RoleRepository repository, RoleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RoleOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public RoleOutput getElemById(Short id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public RoleEntity get(Short id) {
        return repository.getById(id);
    }

    public RoleEntity save(RoleEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(Short id) {
        repository.deleteById(id);
    }

    public void putById(Short id, RoleInput input) {
        RoleEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

}
