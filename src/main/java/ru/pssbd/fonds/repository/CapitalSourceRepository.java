package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CapitalSourceEntity;

import java.math.BigInteger;

@Repository
public interface CapitalSourceRepository extends JpaRepository<CapitalSourceEntity, BigInteger> {
}
