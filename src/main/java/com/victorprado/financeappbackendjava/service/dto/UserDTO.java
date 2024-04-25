package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

  private String id;
  private String name;
  private String lastname;
  private String email;
  private BigDecimal salary;
  private BigDecimal taxValue;
  private BigDecimal exchangeTaxValue;
  @Valid
  private List<CreditCardDTO> creditCards = new ArrayList<>();
  @Valid
  private List<TransactionDTO> transactions = new ArrayList<>();
  @Valid
  private List<RecurringExpenseDTO> recurringExpenses = new ArrayList<>();
  private List<MonthClosureDTO> monthClosures = new ArrayList<>();
}
