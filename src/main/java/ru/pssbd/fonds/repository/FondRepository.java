package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.FondEntity;

@Repository
public interface FondRepository extends JpaRepository<FondEntity, Integer> {
}
