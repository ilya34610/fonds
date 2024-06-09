package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FondRepository extends JpaRepository<FondEntity, Integer> {

    @Query("SELECT f AS fond, cs AS capitalSource FROM FondEntity f JOIN f.capitalSources cs WHERE cs.user = :user")
    List<Object[]> findAllFondsByUser(@Param("user") UserEntity user);

//    @Query("SELECT f AS fond, u AS users FROM FondEntity f JOIN f.users cs WHERE u.user = :user")
//    FondOutput findAllElemByCurrentStaff(@Param("user") UserEntity user);

    @Query("SELECT f FROM FondEntity f WHERE f.user = :user")
    List<FondEntity> findAllElemByCurrentStaff(@Param("user") UserEntity user);

    @Modifying
    @Query("UPDATE FondEntity f SET f.sum = f.sum+ :sum WHERE f.id = :id ")
    void plusBalance(@Param("sum") BigDecimal sum, @Param("id") Integer id);


    @Transactional
    @Modifying
    @Query("UPDATE FondEntity f SET f.sum = f.sum - :sum WHERE f.id = :id ")
    void minusBalance(@Param("sum") BigDecimal sum, @Param("id") Integer id);

//
//    findAllElemByCurrentDonater
}
