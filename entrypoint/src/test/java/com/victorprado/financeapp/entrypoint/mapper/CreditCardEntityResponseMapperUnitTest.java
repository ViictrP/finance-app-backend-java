package com.victorprado.financeapp.entrypoint.mapper;

import static org.assertj.core.api.BDDAssertions.then;

import com.victorprado.financeapp.core.entities.CreditCard;
import com.victorprado.financeapp.entrypoint.request.CreditCardRequest;
import com.victorprado.financeapp.entrypoint.response.CreditCardResponse;
import org.junit.jupiter.api.Test;

class CreditCardEntityResponseMapperUnitTest {

  final CreditCardEntityResponseMapper mapper = new CreditCardEntityResponseMapperImpl();

  @Test
  void given_a_credit_entity_card_then_should_return_credit_card_response() {
    var creditCard = CreditCard.builder()
      .number("0000")
      .build();

    var response = mapper.toResponse(creditCard);

    then(response).usingRecursiveComparison().isEqualTo(creditCard);
  }

  @Test
  void given_a_null_credit_card_entity_then_should_return_null() {
    var response = mapper.toResponse(null);

    then(response).isNull();
  }

  @Test
  void given_a_credit_card_response_then_should_return_credit_card_entity() {
    var response = CreditCardResponse.builder()
      .number("0000")
      .build();

    var creditCard = mapper.toEntity(response);

    then(creditCard.getNumber()).isEqualTo(response.getNumber());
  }

  @Test
  void given_a_null_credit_card_response_then_should_return_null() {
    var creditCard = mapper.toEntity(null);

    then(creditCard).isNull();
  }

  @Test
  void given_a_credit_card_request_then_should_return_credit_card_entity() {
    var request = new CreditCardRequest();
    request.setNumber("0000");

    var creditCard = mapper.fromRequest(request);

    then(creditCard.getNumber()).isEqualTo(request.getNumber());
  }

  @Test
  void given_a_null_credit_card_request_then_should_return_null() {
    var creditCard = mapper.fromRequest(null);

    then(creditCard).isNull();
  }
}
