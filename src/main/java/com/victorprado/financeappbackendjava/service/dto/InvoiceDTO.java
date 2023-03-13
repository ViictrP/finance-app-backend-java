package com.victorprado.financeappbackendjava.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class InvoiceDTO {
  private Boolean isClosed = Boolean.FALSE;
  private Integer year;
  private String month;

  private List<TransactionDTO> transactions;
}
