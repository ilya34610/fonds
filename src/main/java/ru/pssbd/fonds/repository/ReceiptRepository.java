package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pssbd.fonds.entity.ReceiptEntity;

import java.math.BigInteger;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, BigInteger> {
}
