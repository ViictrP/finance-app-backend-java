package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

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
