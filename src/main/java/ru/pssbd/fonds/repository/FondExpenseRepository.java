package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.pssbd.fonds.entity.FondExpenseEntity;

import java.math.BigInteger;


public interface FondExpenseRepository extends JpaRepository<FondExpenseEntity, BigInteger> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO fonds_fond_expenses (id_fonds, id_fond_expenses) VALUES (:idFond, :idFondExpenses)", nativeQuery = true)
    void saveTransaction(@Param("idFond") Integer idFond, @Param("idFondExpenses") Integer idFondExpenses);




}
