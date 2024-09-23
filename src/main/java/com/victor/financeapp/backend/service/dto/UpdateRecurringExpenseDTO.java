package com.victor.financeapp.backend.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateRecurringExpenseDTO {
    private String description;
    private String category;
    private BigDecimal amount;
}
