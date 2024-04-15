package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionDTO extends BaseDTO {
  private Long userId;

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The category is required")
  private String category;

  @NotNull(message = "The amount is required")
  private BigDecimal amount;

  private Boolean isInstallment = false;
  private Integer installmentNumber;
  private String installmentId;

  private Long creditCardId;

  @NotNull(message = "The date is required")
  private LocalDateTime date;
}
