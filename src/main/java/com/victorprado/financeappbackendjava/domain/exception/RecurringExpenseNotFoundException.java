package com.victorprado.financeappbackendjava.domain.exception;

import org.springframework.http.HttpStatus;

public class RecurringExpenseNotFoundException extends CoreException {

    public RecurringExpenseNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Recurring expense not found!");
    }
}
