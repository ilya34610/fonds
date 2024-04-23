package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CityEntity;
import ru.pssbd.fonds.entity.CountryEntity;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, BigInteger> {

    @Query("SELECT c FROM CountryEntity c ")
    List<CountryEntity> findAllCountries();

}
