package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.entity.CreditCard;
import com.victor.financeapp.backend.domain.entity.Invoice;
import com.victor.financeapp.backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
  List<Invoice> findByCreditCard(CreditCard creditCard);
  Optional<Invoice> findByMonthAndYearAndCreditCardId(String month, Integer year, Long creditCardId);
  List<Invoice> findByMonthAndYearAndCreditCardIn(String month, Integer year, List<CreditCard> creditCard);
  List<Invoice> findByCreditCardAndMonthAndYearAndCreditCard_User(CreditCard creditCard, String month, Integer year, User user);

}
