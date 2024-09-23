package com.victor.financeapp.backend.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalaryDTO extends BaseDTO {
  private String userId;
  @NotNull(message = "The value is required")
  private BigDecimal value;
  @NotBlank(message = "The month is required")
  private String month;
  @NotNull(message = "The year is required")
  private Integer year;
}
