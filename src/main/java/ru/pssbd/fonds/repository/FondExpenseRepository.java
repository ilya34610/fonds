package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pssbd.fonds.entity.FondExpenseEntity;

import java.math.BigInteger;


public interface FondExpenseRepository extends JpaRepository<FondExpenseEntity, BigInteger> {
}
