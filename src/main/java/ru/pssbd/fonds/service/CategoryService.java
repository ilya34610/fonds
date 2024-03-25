package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.repository.CapitalSourceRepository;

@Service
public class CategoryService {

    private final CapitalSourceRepository repository;

    public CategoryService(CapitalSourceRepository repository) {
        this.repository = repository;
    }
}
