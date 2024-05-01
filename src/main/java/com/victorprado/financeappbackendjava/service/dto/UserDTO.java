package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private BigDecimal nonConvertedSalary;
  @Valid
  private List<CreditCardDTO> creditCards = new ArrayList<>();
  @Valid
  private List<TransactionDTO> transactions = new ArrayList<>();
  @Valid
  private List<RecurringExpenseDTO> recurringExpenses = new ArrayList<>();
  private List<MonthClosureDTO> monthClosures = new ArrayList<>();
  private Map<String, String> properties = new HashMap<>();

}
