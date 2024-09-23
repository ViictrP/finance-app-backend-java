package com.victor.financeapp.backend.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreditCardDTO extends BaseDTO {

  private String userId;

  @NotBlank(message = "The title is required")
  private String title;

  @NotNull(message = "The invoice closing day is required")
  private Integer invoiceClosingDay;

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The number is required")
  @Size(min = 4, max = 4, message = "The number should contain ${max} characters")
  private String number;

  private String backgroundColor = "bg-zinc-900";

  private List<InvoiceDTO> invoices;
}
