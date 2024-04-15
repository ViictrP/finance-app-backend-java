package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted = false")
public class CreditCard extends BaseEntity<Long> {
  private static final String DEFAULT_BACKGROUND_COLOR = "bg-zinc-900";

  @NotBlank(message = "The title is required")
  private String title;

  @NotNull(message = "The invoice closing day is required")
  private Integer invoiceClosingDay;

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The number is required")
  @Size(min = 4, max = 4, message = "The number should contain ${max} characters")
  private String number;

  private String backgroundColor = DEFAULT_BACKGROUND_COLOR;

  @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Invoice> invoices;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public CreditCard(CreditCard creditCard, Invoice invoice) {
    this.setId(creditCard.getId());
    this.setCreatedAt(creditCard.getCreatedAt());
    this.setModificatedAt(creditCard.getModificatedAt());
    this.setDeleted(creditCard.isDeleted());
    this.invoices = new ArrayList<>();
    this.invoiceClosingDay = creditCard.invoiceClosingDay;
    this.description = creditCard.description;
    this.title = creditCard.title;
    this.number = creditCard.number;
    this.backgroundColor = creditCard.backgroundColor;
    if (invoice != null) {
      this.invoices.add(invoice);
    }
  }

  @Override
  public boolean validate() {
    return true;
  }
}
