package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT u FROM UserEntity u WHERE u.login = :login")
    Optional<UserEntity> findByLogin(@Param("login") String login);


    Optional<UserEntity> findById(@Param("login") String login);

    @Query("SELECT u AS user, r as role FROM UserEntity u JOIN u.role r WHERE r.name = 'STAFF'")
    List<UserEntity> getStaffs();

    List<UserEntity> findByCanLoginTrueOrderByIdAsc();

    List<UserEntity> findByCanLoginFalseOrderByIdAsc();

//    @Query("UPDATE u")
//    void updateByLogin(String userLogin);

}
