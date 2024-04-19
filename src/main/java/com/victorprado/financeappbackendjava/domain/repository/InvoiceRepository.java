package com.victorprado.financeappbackendjava.domain.repository;

import com.victorprado.financeappbackendjava.domain.entity.CreditCard;
import com.victorprado.financeappbackendjava.domain.entity.Invoice;
import java.util.List;
import java.util.Optional;

import com.victorprado.financeappbackendjava.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
  List<Invoice> findByCreditCard(CreditCard creditCard);

  Optional<Invoice> findByMonthAndYearAndCreditCardId(String month, Integer year, Long creditCardId);

  List<Invoice> findByCreditCardAndMonthAndYearAndCreditCard_User(CreditCard creditCard, String month, Integer year, User user);

//  @Query("""
//          select i from Invoice i inner join CreditCard c on i.creditCard = c inner join FinanceAppUser u on c.user = u\
//          where i.month = :month and i.year = :year and u.id = :userId""")
//  List<Invoice> findByUserMonthAndYear(Long userId, String month, Integer year);
}
