package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.repository.CitizenRepository;

@Service
public class CitizenService {

    private final CitizenRepository repository;

    public CitizenService(CitizenRepository repository) {
        this.repository = repository;
    }
}
