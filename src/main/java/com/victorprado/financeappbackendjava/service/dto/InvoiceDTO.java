package com.victorprado.financeappbackendjava.service.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceDTO extends BaseDTO {
  private Boolean isClosed = Boolean.FALSE;
  private Integer year;
  private String month;

  private List<TransactionDTO> transactions;
}
