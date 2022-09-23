package com.victorprado.financeapp.entrypoint.exception;

import static org.assertj.core.api.BDDAssertions.then;

import com.victorprado.financeapp.core.exceptions.CoreException;
import com.victorprado.financeapp.core.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerUnitTest {

  final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void given_a_core_exception_then_should_return_exception_response_with_same_message() {
    var coreException = new CoreException("Test Message");
    var response = handler.handleCoreException(coreException);

    then(response.getBody().getMessage()).isEqualTo(coreException.getMessage());
    then(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @Test
  void given_a_database_exception_then_should_return_exception_response_with_same_message() {
    var databaseException = new DatabaseException("Test Message");
    var response = handler.handleDatabaseException(databaseException);

    then(response.getBody().getMessage()).isEqualTo(databaseException.getMessage());
    then(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void given_a_exception_then_should_return_exception_response_with_same_message_and_status_internal_server_error() {
    var exception = new Exception("An internal server error occured");
    var response = handler.handleException(exception);

    then(response.getBody().getMessage()).isEqualTo("An internal server error occured");
    then(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
