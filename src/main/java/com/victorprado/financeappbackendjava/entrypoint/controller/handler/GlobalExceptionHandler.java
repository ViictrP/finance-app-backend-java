package com.victorprado.financeappbackendjava.entrypoint.controller.handler;

import com.victorprado.financeappbackendjava.domain.exception.CoreException;
import org.hibernate.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final String RESOURCE_HAS_ALREADY_BEEN_SAVED = "The resource has already been saved.";

  @ExceptionHandler(CoreException.class)
  public ResponseEntity<String> handleCoreExceptions(CoreException exception) {
    return ResponseEntity.status(exception.getStatus())
      .body(exception.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleCoreExceptions(Exception exception) {
    return ResponseEntity.status(500)
            .body("Internal Server Error");
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(TransactionException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateRegisterException(TransactionException exception) {
    return new ResponseEntity<>(Map.of("message", RESOURCE_HAS_ALREADY_BEEN_SAVED), HttpStatus.CONFLICT);
  }
}
