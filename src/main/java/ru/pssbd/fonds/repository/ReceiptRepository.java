package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pssbd.fonds.entity.ReceiptEntity;

import java.math.BigInteger;
import java.util.List;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, BigInteger> {

//    @Modifying
//    @Query("INSERT INTO ReceiptEntity r (sum,fond) VALUES (:sum, :id) JOIN r.capitalSources cs WHERE cs.id = :id")
//    @Query("INSERT INTO ReceiptEntity r (sum,fond) VALUES (:sum, :id)")
//    void plusNewReceipt(@Param("sum") BigDecimal sum, @Param("id") Integer id );

    @Query("SELECT r AS receipt, cs AS capitalSource FROM CapitalSourceEntity cs JOIN cs.receipts r")
    List<Object[]> getAll();


//    @Modifying
//    @Query(value = "INSERT INTO receipts (sum, id_fond) VALUES (:sum, :id)", nativeQuery = true)
//    void plusNewReceipt(@Param("sum") BigDecimal sum, @Param("id") Integer id);

}
