package com.victorprado.financeappbackendjava.service.dto;

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
  private List<CreditCardDTO> creditCards;
  private List<TransactionDTO> transactions;
  private List<RecurringExpenseDTO> recurringExpenses;
}
