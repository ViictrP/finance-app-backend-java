package com.victorprado.financeapp.entrypoint.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardRequest {

  @NotBlank(message = "Credit card number is required")
  private String number;
}
