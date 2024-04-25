package com.victorprado.financeappbackendjava.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class UserBalanceDTO {
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal available;
    private Map<Long, BigDecimal> creditCardExpenses;
    private List<TransactionDTO> transactions;
    private List<RecurringExpenseDTO> recurringExpenses;
    private List<CreditCardDTO> creditCards;
}
