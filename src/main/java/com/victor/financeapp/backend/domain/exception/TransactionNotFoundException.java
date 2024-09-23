package com.victor.financeapp.backend.domain.exception;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends CoreException {
    public TransactionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "The transaction was not found!");
    }
}
