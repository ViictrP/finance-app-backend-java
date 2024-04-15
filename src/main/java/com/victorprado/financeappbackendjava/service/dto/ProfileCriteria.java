package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileCriteria {
  @NotBlank(message = "Please provide a month")
  private String month;

  @NotBlank(message = "Please provide a year")
  private Integer year;
}
