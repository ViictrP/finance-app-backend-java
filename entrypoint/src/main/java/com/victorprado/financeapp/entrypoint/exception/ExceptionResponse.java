package com.victorprado.financeapp.entrypoint.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class ExceptionResponse {

  private String message;
}
