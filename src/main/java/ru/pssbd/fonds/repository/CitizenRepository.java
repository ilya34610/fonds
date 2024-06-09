package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CitizenEntity;

import java.math.BigDecimal;
import java.math.BigInteger;

@Repository
public interface CitizenRepository extends JpaRepository<CitizenEntity, BigInteger> {


    @Query("SELECT c FROM CitizenEntity c JOIN c.user cu WHERE cu.id = :id")
    CitizenEntity getByIdUser(@Param("id") Integer id);


    @Modifying
    @Query("UPDATE CitizenEntity c SET c.sum = c.sum - :sum WHERE c.id = :id ")
    void minusBalance(@Param("sum") BigDecimal sum, @Param("id") BigInteger id);


//    void plusBalance(@Param("sum") BigDecimal sum, @Param("id") Integer id );

}
