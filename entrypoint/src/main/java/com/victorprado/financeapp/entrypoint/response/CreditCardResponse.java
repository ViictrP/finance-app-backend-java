package com.victorprado.financeapp.entrypoint.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditCardResponse {

  private String number;
}
