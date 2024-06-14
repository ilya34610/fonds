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


    @Query(value = "SELECT c.\"name\", " +
            "COUNT(f.id) as fonds_count " +
            "FROM public.countries c " +
            "JOIN public.cities ci ON c.id = ci.id_country " +
            "JOIN public.fonds f ON ci.id = f.id_city " +
            "GROUP BY c.\"name\" " +
            "HAVING COUNT(f.id) > ( " +
            "    SELECT AVG(sub_count_fonds) " +
            "    FROM ( " +
            "        SELECT COUNT(f.id) as sub_count_fonds " +
            "        FROM public.fonds f " +
            "        INNER JOIN public.cities ci ON f.id_city = ci.id " +
            "        GROUP BY ci.id_country " +
            "    ) as sub_query " +
            ")",
            nativeQuery = true)
    List<Object[]> getElemForHaving();


    @Query(value = "SELECT ci.\"name\", " +
            "COUNT(ct.id) " +
            "FROM public.citizens ct " +
            "JOIN public.cities ci ON ct.id_city = ci.id " +
            "JOIN public.citizens_categories cc ON ct.id = cc.id_citizen " +
            "JOIN public.categories cat ON cc.id_category = cat.id " +
            "GROUP BY ci.\"name\" " +
            "HAVING COUNT(ct.id) >= 1",
            nativeQuery = true)
    List<Object[]> getElemForHavingCount();





}
