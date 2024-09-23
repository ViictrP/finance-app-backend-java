package com.victor.financeapp.backend.domain.exception;

import org.springframework.http.HttpStatus;

public class CreditCardNotFoundException extends CoreException {

    public CreditCardNotFoundException() {
        super(HttpStatus.NOT_FOUND, "The credit card was not found!");
    }
}
