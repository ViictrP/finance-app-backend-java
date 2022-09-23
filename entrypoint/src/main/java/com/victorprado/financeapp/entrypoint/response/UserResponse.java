package com.victorprado.financeapp.entrypoint.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

  private Long id;
  private String name;
  private String lastname;
  private String email;
  private boolean active;
  private LocalDateTime createdAt;
  private BigDecimal salary;
  private List<CreditCardResponse> creditCards;
  private List<TransactionResponse> transactions;
}
