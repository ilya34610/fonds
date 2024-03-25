package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.repository.CurrencyTypeRepository;

@Service
public class CurrencyTypeService {

    private final CurrencyTypeRepository repository;

    public CurrencyTypeService(CurrencyTypeRepository repository) {
        this.repository = repository;
    }
}
