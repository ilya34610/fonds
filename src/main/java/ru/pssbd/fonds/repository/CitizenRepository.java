package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CitizenEntity;

import java.math.BigInteger;

@Repository
public interface CitizenRepository extends JpaRepository<CitizenEntity, BigInteger> {
}
