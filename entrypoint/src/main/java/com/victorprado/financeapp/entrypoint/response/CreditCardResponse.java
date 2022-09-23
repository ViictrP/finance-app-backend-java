package com.victorprado.financeapp.entrypoint.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditCardResponse {

  private Long id;
  private String number;
}
