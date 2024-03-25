package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CityEntity;

import java.math.BigInteger;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, BigInteger> {
}
