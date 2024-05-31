package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CapitalSourceEntity;
import ru.pssbd.fonds.entity.UserEntity;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CapitalSourceRepository extends JpaRepository<CapitalSourceEntity, BigInteger> {

    @Query("SELECT cs FROM CapitalSourceEntity cs WHERE cs.user = :user")
    List<CapitalSourceEntity> findAllElemByCurrentDonater(@Param("user") UserEntity user);

    @Query("SELECT f AS fond, cs AS capitalSource FROM CapitalSourceEntity cs JOIN cs.fonds f")
    List<Object[]> findAllElem();

}
