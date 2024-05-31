package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.FondEntity;
import ru.pssbd.fonds.entity.UserEntity;

import java.util.List;

@Repository
public interface FondRepository extends JpaRepository<FondEntity, Integer> {

    @Query("SELECT f AS fond, cs AS capitalSource FROM FondEntity f JOIN f.capitalSources cs WHERE cs.user = :user")
    List<Object[]> findAllFondsByUser(@Param("user") UserEntity user);

}
