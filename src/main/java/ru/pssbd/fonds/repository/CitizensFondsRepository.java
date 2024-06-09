package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pssbd.fonds.entity.CitizensFondsEntity;

import java.math.BigInteger;

@Repository
public interface CitizensFondsRepository extends JpaRepository<CitizensFondsEntity, Long> {

//    @Modifying
//    @Transactional
//    @Query("INSERT INTO CitizensFondsEntity (citizen, fond) VALUES (:idCitizen, :idFond)")
//    void saveTransaction(@Param("idCitizen") BigInteger idCitizen, @Param("idFond") BigInteger idFond);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO citizens_fonds (id_citizen, id_fond) VALUES (:idCitizen, :idFond)", nativeQuery = true)
    void saveTransaction(@Param("idCitizen") BigInteger idCitizen, @Param("idFond") BigInteger idFond);


}
