package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDTO {

  private String id;
  private String name;
  private String lastname;
  private String email;
  private SalaryDTO salary;
  @Valid
  private List<CreditCardDTO> creditCards = new ArrayList<>();
  @Valid
  private List<TransactionDTO> transactions = new ArrayList<>();
  @Valid
  private List<RecurringExpenseDTO> recurringExpenses = new ArrayList<>();
}
