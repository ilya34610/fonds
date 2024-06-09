package ru.pssbd.fonds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.input.UserInput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public UserOutput getElemById(Integer id) {
        return repository.findById(id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

    public UserEntity get(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }

    public UserEntity save(UserEntity entity) {
        return repository.save(entity);
    }


    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public void putById(Integer id, UserInput input) {
        UserEntity entity = repository.findById(id)
                .map(existngEntity -> mapper.fromInput(input, existngEntity))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        repository.save(entity);
    }

    public UserOutput getRoleByLogin(String login) {
        return repository.findByLogin(login).stream().findFirst().map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с login " + login + " не найден"));
    }

    public List<UserOutput> getAllElemByRoleStaff() {
        return repository.getStaffs().stream().map(mapper::toOutput).collect(Collectors.toList());

    }

}
