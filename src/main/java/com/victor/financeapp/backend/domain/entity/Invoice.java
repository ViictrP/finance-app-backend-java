package com.victor.financeapp.backend.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity<Long> {

  private Boolean isClosed = Boolean.FALSE;

  @NotNull(message = "The year is required")
  private Integer year;

  @NotBlank(message = "The month is required")
  private String month;

  @ManyToOne()
  @JoinColumn(name = "credit_card_id")
  private CreditCard creditCard;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @OrderBy("date DESC")
  private List<Transaction> transactions;

  @Override
  public boolean validate() {
    return true;
  }

  public void addTransaction(Transaction transaction) {
    if (this.transactions == null)
      this.transactions = new ArrayList<>();

    transaction.setInvoice(this);
    this.transactions.add(transaction);
  }
}
