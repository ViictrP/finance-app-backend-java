package com.victorprado.financeappbackendjava.domain.repository;

import com.victorprado.financeappbackendjava.domain.entity.Invoice;
import com.victorprado.financeappbackendjava.domain.entity.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByInvoice(Invoice invoice);
  List<Transaction> findByUserIdAndInvoiceIsNull(String userId);
  List<Transaction> findByUserIdAndInvoiceIsNullAndDateIsBetween(String userId, LocalDateTime from, LocalDateTime to);

}
