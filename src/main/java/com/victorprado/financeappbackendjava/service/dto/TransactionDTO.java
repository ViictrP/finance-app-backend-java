package com.victorprado.financeappbackendjava.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionDTO {
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
}
