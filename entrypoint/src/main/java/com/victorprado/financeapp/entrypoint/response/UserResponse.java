package com.victorprado.financeapp.entrypoint.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserResponse {

  private String name;
  private String lastname;
  private String email;
  private boolean active;
  private LocalDateTime createdAt;
  private BigDecimal salary;
  private List<CreditCardResponse> creditCards;
  private List<TransactionResponse> transactions;
}
