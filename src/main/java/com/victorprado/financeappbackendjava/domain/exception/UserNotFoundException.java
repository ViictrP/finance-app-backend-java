package com.victorprado.financeappbackendjava.domain.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CoreException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found!");
    }
}
