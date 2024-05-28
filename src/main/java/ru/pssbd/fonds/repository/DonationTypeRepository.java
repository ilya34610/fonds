package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pssbd.fonds.entity.DonationTypeEntity;


public interface DonationTypeRepository extends JpaRepository<DonationTypeEntity, Short> {
}
