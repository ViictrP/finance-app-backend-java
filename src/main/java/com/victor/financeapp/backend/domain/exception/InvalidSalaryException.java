package com.victor.financeapp.backend.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidSalaryException extends CoreException {

  public InvalidSalaryException() {
    super(HttpStatus.BAD_REQUEST, "The salary has invalid data.");
  }
}
