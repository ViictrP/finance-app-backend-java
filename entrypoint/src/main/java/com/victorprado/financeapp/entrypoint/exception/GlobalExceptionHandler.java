package com.victorprado.financeapp.entrypoint.exception;

import com.victorprado.financeapp.core.exceptions.CoreException;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CoreException.class)
  public ResponseEntity<ExceptionResponse> handleCoreException(CoreException exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity.unprocessableEntity().body(buildResponse(exception));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionResponse> handleCoreException(
    MethodArgumentNotValidException exception) {
    log.error(exception.getMessage(), exception);

    AtomicReference<String> errorsList = new AtomicReference<>("");

    exception.getAllErrors().stream()
      .map(error -> Objects.requireNonNull(error.getDefaultMessage()))
      .reduce((sum, current) -> sum + ", " + current)
      .ifPresent(errorsList::set);

    return ResponseEntity.badRequest().body(
      ExceptionResponse.builder()
        .message(errorsList.get())
        .build());
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<ExceptionResponse> handleDatabaseException(DatabaseException exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity.internalServerError().body(buildResponse(exception));
  }

  @ExceptionHandler(Exception.class)
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
