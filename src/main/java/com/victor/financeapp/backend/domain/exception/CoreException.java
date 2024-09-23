package com.victor.financeapp.backend.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CoreException extends RuntimeException {

  private final HttpStatus status;

  public CoreException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
