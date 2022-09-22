package com.victorprado.financeapp.entrypoint.exception;

import com.victorprado.financeapp.core.exceptions.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ExceptionResponse> handleCoreException(InvalidDataException exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity.unprocessableEntity().body(buildResponse(exception));
  }

  @ExceptionHandler
  public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity.internalServerError().body(buildResponse(exception));
  }

  private ExceptionResponse buildResponse(Exception exception) {
    return ExceptionResponse.builder()
      .message(exception.getMessage())
      .build();
  }
}
