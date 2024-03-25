package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.repository.FondRepository;

@Service
public class FondService {

    private final FondRepository repository;

    public FondService(FondRepository repository) {
        this.repository = repository;
    }
}
