package com.victorprado.financeapp.entrypoint.request;

import java.math.BigDecimal;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

  @NotBlank(message = "User must have a name")
  private String name;

  @NotBlank(message = "User must have a last name")
  private String lastname;

  @NotBlank(message = "User must have an email")
  @Email(message = "User's email must be a valid email")
  private String email;

  @NotBlank(message = "User must have a password")
  private String password;

  private BigDecimal salary;
}
