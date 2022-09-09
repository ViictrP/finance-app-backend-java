package com.victorprado.financeapp.infra.model;

import static org.assertj.core.api.BDDAssertions.then;

import com.victorprado.financeapp.core.entities.User;
import org.junit.jupiter.api.Test;

class UserModelUnitTest {

  @Test
  void given_user_then_should_return_user_model_with_same_data() {
    User user = User.builder().name("User").build();
    var model = new UserModel(user);

    then(model.getName()).isEqualTo(user.getName());
  }

  @Test
  void given_no_user_then_should_return_user_model_with_no_data() {
    User user = User.builder().name("User").build();
    var model = new UserModel();

    then(model.getName()).isNotEqualTo(user.getName());
  }
}
