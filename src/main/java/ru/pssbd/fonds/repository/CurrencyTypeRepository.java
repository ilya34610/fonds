package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.CurrencyTypeEntity;

@Repository
public interface CurrencyTypeRepository extends JpaRepository<CurrencyTypeEntity, Short> {


//    @Query(value = "SELECT SUM(public.funds.sum), public.currency_type.\"name\" " +
//            "FROM public.funds " +
//            "JOIN public.capital_sources_found ON public.funds.id = public.capital_sources_found.id " +
//            "JOIN public.capital_sources ON public.capital_sources_found.id_capital_source = public.capital_sources.id " +
//            "JOIN public.currency_type ON public.capital_sources.id_currency_type = public.currency_type.id " +
//            "WHERE public.funds.creation_date > '2020-01-01' " +
//            "GROUP BY public.currency_type.\"name\"; ", nativeQuery = true)
//    List<Object[]> getElemForGroupBy();


//
//    @Query("SELECT SUM(f.sum), c.name " +
//            "FROM FundEntity f " +
//            "JOIN f.capitalSourcesFound csf " +
//            "JOIN csf.capitalSource cs " +
//            "JOIN cs.currencyType c " +
//            "WHERE f.creationDate > :creationDate " +
//            "GROUP BY c.name")
//    List<Object[]> getElemForGroupBy(@Param("creationDate") LocalDate creationDate);




}
