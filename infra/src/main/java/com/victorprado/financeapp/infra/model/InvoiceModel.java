package com.victorprado.financeapp.infra.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice", schema = "finance_app")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceModel extends Model {

  private String month;
  private Integer year;
  private boolean isClosed;

  @ManyToOne
  @JoinColumn(name = "credit_card_id", nullable = false)
  private CreditCardModel creditCard;

  @OneToMany(mappedBy = "invoice")
  private List<TransactionModel> transactions;
}
