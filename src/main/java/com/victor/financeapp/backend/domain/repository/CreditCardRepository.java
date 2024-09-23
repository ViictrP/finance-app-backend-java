package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

  @Query("select new CreditCard(c, i) from CreditCard c left outer join Invoice i on i.creditCard = c and i.month = :month and i.year = :year where c.user.id = :userId order by c.id asc")
  List<CreditCard> findByUserAndInvoicesByMonthAndYear(@Param("userId") Long userId,
    @Param("month") String month, @Param("year") Integer year);

  boolean existsByNumber(String number);
}
