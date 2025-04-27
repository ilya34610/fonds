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
import java.math.BigInteger;
import java.util.List;

@Repository
public interface FondRepository extends JpaRepository<FondEntity, BigInteger> {

    @Query("SELECT f FROM FondEntity f WHERE f.capitalSource.user = :user")
    List<FondEntity> findAllFondByUser(@Param("user") UserEntity user);

//    @Query("SELECT f AS fond, u AS users FROM FondEntity f JOIN f.users cs WHERE u.user = :user")
//    FondOutput findAllElemByCurrentStaff(@Param("user") UserEntity user);

    @Query("SELECT f FROM FondEntity f WHERE f.user = :user")
    List<FondEntity> findAllElemByCurrentStaff(@Param("user") UserEntity user);

    @Modifying
    @Query("UPDATE FondEntity f SET f.sum = f.sum+ :sum WHERE f.id = :id ")
    void plusBalance(@Param("sum") BigDecimal sum, @Param("id") BigInteger id);


    @Transactional
    @Modifying
    @Query("UPDATE FondEntity f SET f.sum = f.sum - :sum WHERE f.id = :id ")
    void minusBalance(@Param("sum") BigDecimal sum, @Param("id") BigInteger id);


//    List<FondEntity> findAllElemByCurrentCitizen(@Param("idUser") Integer idUser);

//    @Query("SELECT f AS fond, fe AS fondExpenses FROM FondEntity f JOIN f.fondExpenses fe WHERE fe.citizen.id = :idUser")
//    List<Object[]> findAllElemByCurrentCitizen(@Param("idUser") BigInteger idUser);

//    @Query("SELECT f, fe FROM FondEntity f JOIN f.fondExpenses fe WHERE fe.citizen.id = :idUser")
//    List<Object[]> findAllElemByCurrentCitizen(@Param("idUser") BigInteger idUser);


    @Query("SELECT f, fe FROM FondEntity f " +
            "JOIN f.fondExpenses fe " +
            "JOIN fe.citizen c " +
            "JOIN c.user u " +
            "WHERE u.id = :idUser")
    List<Object[]> findAllElemByCurrentCitizen(@Param("idUser") Integer idUser);


//    @Query(value = "SELECT f.id AS fonds_id, f.name AS fonds_name, f.id_city AS fonds_city, f.creation_date AS fonds_creation_date, f.phone AS fonds_phone, f.id_user AS fonds_user, f.sum AS fonds_sum, fe.id AS fond_expenses_id, fe.sum AS fond_expenses_sum, fe.id_citizen AS fond_expenses_citizen FROM public.fonds f JOIN public.fonds_fond_expenses ffe ON f.id = ffe.id_fonds JOIN public.fond_expenses fe ON ffe.id_fond_expenses = fe.id WHERE fe.id_citizen = :idUser", nativeQuery = true)
//    List<Object[]> findAllElemByCurrentCitizen(@Param("idUser") BigInteger idUser);

//    SELECT
//    f.id AS fonds_id,
//    f.name AS fonds_name,
//    f.id_city AS fonds_city,
//    f.creation_date AS fonds_creation_date,
//    f.phone AS fonds_phone,
//    f.id_user AS fonds_user,
//    f.sum AS fonds_sum,
//    fe.id AS fond_expenses_id,
//    fe.sum AS fond_expenses_sum,
//    fe.id_citizen AS fond_expenses_citizen
//            FROM
//    public.fonds f
//    JOIN
//    public.fonds_fond_expenses ffe ON f.id = ffe.id_fonds
//            JOIN
//    public.fond_expenses fe ON ffe.id_fond_expenses = fe.id
//            WHERE
//    fe.id_citizen = <id_citizen>;


    @Query(value = "SELECT f.*, " +
            "CASE " +
            "WHEN f.id IN ( " +
            "  SELECT cf.id_fond " +
            "  FROM public.citizens_fonds cf " +
            ") THEN 'Да' " +
            "ELSE 'Нет' " +
            "END AS has_citizens " +
            "FROM public.fonds f",
            nativeQuery = true)
    List<Object[]> getElemRequestWithCase();


    @Query(value = "SELECT * " +
            "FROM public.fonds f " +
            "WHERE f.id NOT IN (" +
            "  SELECT cf.id_fond " +
            "  FROM public.citizens_fonds cf" +
            ")", nativeQuery = true)
    List<Object[]> getElemQueryNotIn();





}
