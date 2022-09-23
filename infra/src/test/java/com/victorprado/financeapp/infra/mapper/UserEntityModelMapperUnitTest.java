package com.victorprado.financeapp.infra.mapper;

import static org.assertj.core.api.BDDAssertions.then;

import com.victorprado.financeapp.core.entities.CreditCard;
import com.victorprado.financeapp.core.entities.Transaction;
import com.victorprado.financeapp.core.entities.User;
import com.victorprado.financeapp.infra.model.CreditCardModel;
import com.victorprado.financeapp.infra.model.TransactionModel;
import com.victorprado.financeapp.infra.model.UserModel;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserEntityModelMapperUnitTest {

  final UserEntityModelMapper mapper = new UserEntityModelMapperImpl();

  @Test
  void given_a_valid_user_then_should_return_user_model() {
    var user = User.builder()
      .name("Test")
      .lastname("Test")
      .active(true)
      .password("Test")
      .creditCards(List.of(CreditCard.builder().build()))
      .transactions(List.of(Transaction.builder().build()))
      .createdAt(LocalDateTime.now())
      .build();

    var model = mapper.toModel(user);

    then(model).usingRecursiveComparison()
      .ignoringFields("id", "createdAt", "creditCards", "transactions")
      .isEqualTo(user);
  }

  @Test
  void given_null_user_then_should_return_null() {
    var model = mapper.toModel(null);

    then(model).isNull();
  }

  @Test
  void given_a_valid_user_with_empty_credit_cards_and_transactions_then_should_return_user_model() {
    var user = User.builder()
      .name("Test")
      .lastname("Test")
      .active(true)
      .password("Test")
      .creditCards(List.of())
      .transactions(List.of())
      .createdAt(LocalDateTime.now())
      .build();

    var model = mapper.toModel(user);

    then(model).usingRecursiveComparison()
      .ignoringFields("id", "createdAt", "creditCards", "transactions")
      .isEqualTo(user);
  }

  @Test
  void given_a_valid_user_without_credit_cards_and_transactions_then_should_return_user_model() {
    var user = User.builder()
      .name("Test")
      .lastname("Test")
      .active(true)
      .password("Test")
      .creditCards(null)
      .transactions(null)
      .createdAt(LocalDateTime.now())
      .build();

    var model = mapper.toModel(user);

    then(model).usingRecursiveComparison()
      .ignoringFields("id", "createdAt", "creditCards", "transactions")
      .isEqualTo(user);
  }

  @Test
  void given_a_user_model_then_should_return_user() {
    var model = UserModel.builder()
      .name("Test")
      .lastname("Test")
      .active(true)
      .password("Test")
      .creditCards(List.of(CreditCardModel.builder().build()))
      .transactions(List.of(TransactionModel.builder().build()))
      .build();

    var user = mapper.toEntity(model);

    then(user).usingRecursiveComparison()
      .ignoringFields("id", "createdAt", "creditCards", "transactions")
      .isEqualTo(model);
  }

  @Test
  void given_a_null_user_model_then_should_return_null() {
    var user = mapper.toEntity(null);

    then(user).isNull();
  }

  @Test
  void given_a_valid_user_model_without_credit_cards_and_transactions_then_should_return_user() {
    var model = UserModel.builder()
      .name("Test")
      .lastname("Test")
      .active(true)
      .password("Test")
      .creditCards(null)
      .transactions(null)
      .build();

    var user = mapper.toEntity(model);

    then(model).usingRecursiveComparison()
      .ignoringFields("id", "createdAt", "creditCards", "transactions")
      .isEqualTo(user);
  }

  @Test
  void given_a_valid_user_model_with_empty_credit_cards_and_transactions_then_should_return_user() {
    var model = UserModel.builder()
      .name("Test")
      .lastname("Test")
      .active(true)
      .password("Test")
      .creditCards(List.of())
      .transactions(List.of())
      .build();

    var user = mapper.toEntity(model);

    then(model).usingRecursiveComparison()
      .ignoringFields("id", "createdAt", "creditCards", "transactions")
      .isEqualTo(user);
  }
}
