package com.victor.financeapp.backend.service.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCreditCardDTO {
    private String title;
    private Integer invoiceClosingDay;
    private String description;

    @Size(min = 4, max = 4, message = "The number should contain ${max} characters")
    private String number;

    private String backgroundColor;
}
