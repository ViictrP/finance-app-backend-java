package com.victor.financeapp.backend.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
  private Integer installmentAmount;
  private String installmentId;
  private Integer installmentNumber;

  private Long creditCardId;

  @NotNull(message = "The date is required")
  private LocalDateTime date;
}
