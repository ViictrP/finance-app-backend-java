package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecurringExpenseDTO extends BaseDTO {
  @NotBlank(message = "The user ID is required")
  private String userId;

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The category is required")
  private String category;

  @NotNull(message = "The amount is required")
  private BigDecimal amount;
}
