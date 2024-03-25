package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.DonationTypeEntity;

@Repository
public interface DonationTypeRepository extends JpaRepository<DonationTypeEntity, Short> {
}
