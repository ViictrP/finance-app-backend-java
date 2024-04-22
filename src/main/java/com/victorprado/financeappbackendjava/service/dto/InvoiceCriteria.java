package com.victorprado.financeappbackendjava.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceCriteria extends ProfileCriteria {
    private Long creditCardId;
}
