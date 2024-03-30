package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.dto.output.DonationOutput;
import ru.pssbd.fonds.mappers.DonationMapper;
import ru.pssbd.fonds.repository.DonationRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DonationService {

    private final DonationRepository repository;
    private final DonationMapper mapper;

    public DonationService(DonationRepository repository, DonationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DonationOutput> getAllElem() {
        return repository.findAll().stream()
                .map(mapper::toOutput)
                .collect(Collectors.toList());
    }

    public DonationOutput getElemById(int id) {
        return repository.findById(BigInteger.valueOf(id))
                .map(mapper::toOutput)
                .orElseThrow(() -> new NoSuchElementException("Элемент с id " + id + " не найден"));
    }

}
