package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.repository.CapitalSourceRepository;

@Service
public class CapitalSourceService {

    private final CapitalSourceRepository repository;

    public CapitalSourceService(CapitalSourceRepository repository) {
        this.repository = repository;
    }
}
