package ru.pssbd.fonds.service;

import org.springframework.stereotype.Service;
import ru.pssbd.fonds.repository.DonationRepository;

@Service
public class DonationTypeService {

    private final DonationRepository repository;

    public DonationTypeService(DonationRepository repository) {
        this.repository = repository;
    }
}
