package com.victorprado.financeapp.entrypoint.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponse {

  private Long id;
  private String description;
  private LocalDateTime createdAt;
}
