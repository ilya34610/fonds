package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.repository.CityRepository;

@Service
public class CityService {

    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }
}
