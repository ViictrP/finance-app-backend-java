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
  List<Invoice> findByMonthAndYearAndCreditCardIn(String month, Integer year, List<CreditCard> creditCard);
  List<Invoice> findByCreditCardAndMonthAndYearAndCreditCard_User(CreditCard creditCard, String month, Integer year, User user);

}
