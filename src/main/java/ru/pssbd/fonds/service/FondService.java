package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.FondCapitalSourceOutput;
import ru.pssbd.fonds.dto.output.FondOutput;
import ru.pssbd.fonds.dto.output.UserOutput;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.entity.UserEntity;
import ru.pssbd.fonds.mappers.FondMapper;
import ru.pssbd.fonds.mappers.UserMapper;
import ru.pssbd.fonds.repository.FondRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FondService {

    private final UserService userService;
    private final UserMapper userMapper;

    private final FondRepository repository;
    private final FondMapper mapper;

    public FondService(UserService userService, UserMapper userMapper, FondRepository repository, FondMapper mapper) {
        this.userService = userService;
        this.userMapper = userMapper;
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

    public List<FondCapitalSourceOutput> getAllFondsForCurrentDonater(UserOutput donater) {
        List<FondCapitalSourceOutput> result = new ArrayList<>();
        UserEntity userEntity = userService.get(donater.getId());

        List<Object[]> data = repository.findAllFondsByUser(userEntity);
        if (!data.isEmpty()) {

            for (Object[] row : data) {
                FondEntity fond = (FondEntity) row[0];
                CapitalSourceEntity capitalSource = (CapitalSourceEntity) row[1];
                result.add(new FondCapitalSourceOutput(fond, capitalSource));
            }
            return result;
        } else {
            return result;
        }

    }

}
