package com.victorprado.financeappbackendjava.domain.repository;

import com.victorprado.financeappbackendjava.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByUserIdAndInvoiceIsNullAndDateIsBetween(Long userId, LocalDateTime from, LocalDateTime to);
}
