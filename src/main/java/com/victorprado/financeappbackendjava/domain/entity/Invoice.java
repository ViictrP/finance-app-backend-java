package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invoice extends BaseEntity<Long> {
  private Boolean isClosed = Boolean.FALSE;
  @NotNull(message = "The year is required")
  private Integer year;
  @NotBlank(message = "The month is required")
  private String month;

  @ManyToOne()
  @JoinColumn(name = "credit_card_id")
  private CreditCard creditCard;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Transaction> transactions;

  @Override
  public boolean validate() {
    return true;
  }
}
