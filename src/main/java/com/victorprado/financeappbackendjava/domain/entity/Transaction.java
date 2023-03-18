package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction extends BaseEntity<Long> {

  @NotBlank(message = "The user ID is required")
  private String userId;

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The category is required")
  private String category;

  @NotNull(message = "The amount is required")
  private BigDecimal amount;

  private Boolean isInstallment = false;
  private Integer installmentNumber;
  private String installmentId;

  @NotNull(message = "The date is required")
  private LocalDateTime date;

  @ManyToOne()
  @JoinColumn(name = "invoice_id")
  private Invoice invoice;

  @Override
  public boolean validate() {
    return true;
  }
}
