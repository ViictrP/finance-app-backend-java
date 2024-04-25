package com.victorprado.financeappbackendjava.domain.exception;

import org.springframework.http.HttpStatus;

public class CreditCardAlreadyExistsException extends CoreException {
    public CreditCardAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "The credit card already exists!");
    }
}
