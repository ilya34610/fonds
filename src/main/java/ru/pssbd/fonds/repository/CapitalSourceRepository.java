package ru.pssbd.fonds.repository;

import org.springframework.data.domain.Pageable;
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


    @Query(value = "SELECT public.currency_type.id, " +
            "public.currency_type.name, " +
            "public.capital_sources.sum, " +
            "public.capital_sources.donation_date, " +
            "public.capital_sources.id_user " +
            "FROM public.currency_type " +
            "LEFT JOIN public.capital_sources " +
            "ON public.currency_type.id = public.capital_sources.id_currency_type", nativeQuery = true)
    List<Object[]> getAllCapitalSourcesLeftJoin();


    @Query(value = "SELECT dt.id, " +
            "dt.name, " +
            "cs.sum, " +
            "cs.donation_date, " +
            "cs.id_user " +
            "FROM public.capital_sources_donation_types csdt " +
            "RIGHT JOIN public.capital_sources cs " +
            "ON csdt.id_capital_source = cs.id " +
            "RIGHT JOIN public.donation_types dt " +
            "ON csdt.id_donation_type = dt.id ",
            nativeQuery = true)
    List<Object[]> getAllCapitalSourcesRightJoin();


//    @Query("SELECT с AS capitalSource FROM CapitalSourceEntity cs ")
//    List<CapitalSourceEntity> getTopThreeDonator();

    @Query("SELECT cs FROM CapitalSourceEntity cs ORDER BY cs.sum DESC")
    List<CapitalSourceEntity> getTopThreeDonator(Pageable pageable);



}
