package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalaryDTO extends BaseDTO {
  @NotBlank(message = "The user ID is required")
  private String userId;
  @NotNull(message = "The value is required")
  private BigDecimal value;
  @NotBlank(message = "The month is required")
  private String month;
  @NotNull(message = "The year is required")
  private Integer year;
}
