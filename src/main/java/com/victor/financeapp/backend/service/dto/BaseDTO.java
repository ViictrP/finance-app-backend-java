package com.victor.financeapp.backend.service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDTO {
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime modificatedAt;
  private boolean deleted;
}
