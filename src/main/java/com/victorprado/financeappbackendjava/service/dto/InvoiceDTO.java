package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceDTO extends BaseDTO {
  private Boolean isClosed = Boolean.FALSE;
  @NotNull(message = "The year is required")
  private Integer year;
  @NotBlank(message = "The month is required")
  private String month;

  private List<TransactionDTO> transactions;
}
