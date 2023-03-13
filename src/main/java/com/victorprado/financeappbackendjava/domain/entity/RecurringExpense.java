package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.Entity;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecurringExpense extends BaseEntity<Long> {

  @NotBlank(message = "The user ID is required")
  private String userId;

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The category is required")
  private String category;

  @NotNull(message = "The amount is required")
  private BigDecimal amount;

  @Override
  public Boolean validate() {
    return null;
  }
}
