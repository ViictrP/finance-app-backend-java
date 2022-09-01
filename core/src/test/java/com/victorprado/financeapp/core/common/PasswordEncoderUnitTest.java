package com.victorprado.financeapp.core.common;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class PasswordEncoderUnitTest {

  @Test
  void given_a_valid_password_when_encoding_then_return_encoded_password() {
    String password = "123456";
    String encryptedPassword = PasswordEncoder.encode(password);

    then(encryptedPassword).isNotBlank();
  }

  @Test
  void given_a_encoded_password_when_validating_then_return_true_if_matches() {
    String password = "123456";
    String encryptedPassword = PasswordEncoder.encode(password);

    then(PasswordEncoder.validate(password, encryptedPassword)).isTrue();
  }

  @Test
  void given_a_invalid_encoded_password_when_validating_then_return_false() {
    String password = "123456";
    String encryptedPassword = "encoded";

    then(PasswordEncoder.validate(password, encryptedPassword)).isFalse();
  }
}
