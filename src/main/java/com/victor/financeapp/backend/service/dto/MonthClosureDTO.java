package com.victor.financeapp.backend.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MonthClosureDTO {
    private String month;
    private Integer year;
    private BigDecimal total;
    private BigDecimal available;
    private BigDecimal expenses;
    private Integer index;
}
