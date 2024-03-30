package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.DonationTypeOutput;
import ru.pssbd.fonds.mappers.DonationTypeMapper;
import ru.pssbd.fonds.repository.DonationTypeRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DonationTypeService {

    private final DonationTypeRepository repository;
    private final DonationTypeMapper mapper;

    public DonationTypeService(DonationTypeRepository repository, DonationTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DonationTypeOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public DonationTypeOutput getElemById(int id) {
        return repository.findById((short) id)
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

}
