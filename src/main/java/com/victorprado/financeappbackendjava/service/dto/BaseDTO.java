package com.victorprado.financeappbackendjava.service.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BaseDTO {
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime modificatedAt;
  private boolean deleted;
}
