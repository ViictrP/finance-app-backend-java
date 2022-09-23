package com.victorprado.financeapp.entrypoint.mapper;

import static org.assertj.core.api.BDDAssertions.then;

import com.victorprado.financeapp.core.entities.Transaction;
import com.victorprado.financeapp.entrypoint.request.TransactionRequest;
import com.victorprado.financeapp.entrypoint.response.TransactionResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TransactionEntityResponseMapperUnitTest {

  final TransactionEntityResponseMapper mapper = new TransactionEntityResponseMapperImpl();

  @Test
  void given_a_transaction_entity_then_should_return_transaction_response() {
    var transaction = Transaction.builder()
      .description("Test")
      .build();

    var response = mapper.toResponse(transaction);

    then(response).usingRecursiveComparison().isEqualTo(transaction);
  }

  @Test
  void given_a_null_transaction_entity_then_should_return_null() {
    var response = mapper.toResponse(null);

    then(response).isNull();
  }

  @Test
  void given_a_transaction_response_then_should_return_transaction_entity() {
    var response = TransactionResponse.builder()
      .description("Teste")
      .id("Test")
      .createdAt(LocalDateTime.now())
      .build();

    var transaction = mapper.toEntity(response);

    then(transaction).usingRecursiveComparison()
      .ignoringFields("id", "createdAt")
      .isEqualTo(response);
  }

  @Test
  void given_a_null_transaction_response_then_should_return_null() {
    var transaction = mapper.toEntity(null);

    then(transaction).isNull();
  }

  @Test
  void given_a_transaction_request_then_should_return_transaction_entity() {
    var request = new TransactionRequest();

    var transaction = mapper.fromRequest(request);

    then(transaction.getDescription()).isEqualTo(request.getDescription());
  }

  @Test
  void given_a_null_transaction_request_then_should_return_null() {
    var transaction = mapper.fromRequest(null);

    then(transaction).isNull();
  }
}
