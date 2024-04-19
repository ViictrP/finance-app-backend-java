package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceCriteria extends ProfileCriteria {

    @NotNull(message = "The credit card ID is required for the search")
    private Long creditCardId;
}
