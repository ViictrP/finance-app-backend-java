package com.victorprado.financeapp.infra.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction", schema = "finance_app")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel extends Model {

  private BigDecimal amount;
  private String description;
  private boolean isInstallment;
  private Integer installmentAmount;
  private Integer installmentNumber;
  private LocalDateTime date;
  private String category;

  @ManyToOne
  @JoinColumn(name = "invoice_id")
  private InvoiceModel invoice;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserModel user;
}
