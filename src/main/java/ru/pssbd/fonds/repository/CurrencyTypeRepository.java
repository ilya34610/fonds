package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CurrencyTypeEntity;

@Repository
public interface CurrencyTypeRepository extends JpaRepository<CurrencyTypeEntity, Short> {
}
