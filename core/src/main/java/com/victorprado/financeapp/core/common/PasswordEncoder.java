package com.victorprado.financeapp.core.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordEncoder {

  private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

  public static String encode(String password) {
    return ENCODER.encode(password);
  }

  public static boolean validate(String password, String encryptedPassword) {
    return ENCODER.matches(password, encryptedPassword);
  }
}
