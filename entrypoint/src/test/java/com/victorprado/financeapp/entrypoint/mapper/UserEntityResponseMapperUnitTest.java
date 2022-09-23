package com.victorprado.financeapp.entrypoint.mapper;

import static org.assertj.core.api.BDDAssertions.then;

import com.victorprado.financeapp.core.entities.CreditCard;
import com.victorprado.financeapp.core.entities.Transaction;
import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.entrypoint.request.UserRequest;
import com.victorprado.financeapp.entrypoint.response.CreditCardResponse;
import com.victorprado.financeapp.entrypoint.response.TransactionResponse;
import com.victorprado.financeapp.entrypoint.response.UserResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserEntityResponseMapperUnitTest {

  final CreditCardEntityResponseMapper creditCardMapper = Mockito.mock(
    CreditCardEntityResponseMapper.class);
  final TransactionEntityResponseMapper transactionMapper = Mockito.mock(
    TransactionEntityResponseMapper.class);
  final UserEntityResponseMapper mapper = new UserEntityResponseMapperImpl(creditCardMapper,
    transactionMapper);

  @Test
  void given_a_user_entity_then_should_return_user_response() {
    var user = User.builder()
      .name("Test")
      .lastname("Test")
      .active(true)
      .password("Test")
      .creditCards(List.of(CreditCard.builder().build()))
      .transactions(List.of(Transaction.builder().build()))
      .createdAt(LocalDateTime.now())
      .createdAt(LocalDateTime.now())
      .build();

    var response = mapper.toResponse(user);

    then(response).usingRecursiveComparison()
      .ignoringFields("creditCards", "transactions")
      .isEqualTo(user);
  }

  @Test
  void given_a_null_user_entity_then_should_return_null() {
    var response = mapper.toResponse(null);

    then(response).isNull();
  }

  @Test
  void given_a_user_response_then_should_return_user_entity() {
    var response = UserResponse.builder()
      .active(true)
      .createdAt(LocalDateTime.now())
      .creditCards(List.of(CreditCardResponse.builder().build()))
      .transactions(List.of(TransactionResponse.builder().build()))
      .email("email@email.com")
      .salary(BigDecimal.ONE)
      .name("Test")
      .lastname("Test")
      .build();

    var user = mapper.toEntity(response);

    then(user).usingRecursiveComparison()
      .ignoringFields("password", "id", "creditCards", "transactions")
      .isEqualTo(response);
  }

  @Test
  void given_a_null_user_response_then_should_return_null() {
    var user = mapper.toEntity(null);

    then(user).isNull();
  }

  @Test
  void given_null_transactions_and_credit_cards_user_response_then_should_return_null_for_those_fields() {
    var response = UserResponse.builder()
      .active(true)
      .createdAt(LocalDateTime.now())
      .creditCards(null)
      .transactions(null)
      .email("email@email.com")
      .salary(BigDecimal.ONE)
      .name("Test")
      .lastname("Test")
      .build();
    var user = mapper.toEntity(response);

    then(user).usingRecursiveComparison().isEqualTo(user);
  }

  @Test
  void given_a_user_request_then_should_return_user_entity() {
    var request = new UserRequest();

    var user = mapper.fromRequest(request);

    then(user.getName()).isEqualTo(request.getName());
  }

  @Test
  void given_a_null_user_request_then_should_return_null() {
    var user = mapper.fromRequest(null);

    then(user).isNull();
  }
}
