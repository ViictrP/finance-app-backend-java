package com.victor.financeapp.backend.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Where(clause = "deleted = false")
public class Transaction extends BaseEntity<Long> {

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The category is required")
  private String category;

  @NotNull(message = "The amount is required")
  private BigDecimal amount;

  private Boolean isInstallment = false;
  private Integer installmentNumber;
  private String installmentId;
  private Integer installmentAmount;

  @NotNull(message = "The date is required")
  private LocalDateTime date;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "invoice_id")
  private Invoice invoice;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Override
  public boolean validate() {
    return true;
  }

  public void delete() {
    this.setDeleted(true);
  }
}
