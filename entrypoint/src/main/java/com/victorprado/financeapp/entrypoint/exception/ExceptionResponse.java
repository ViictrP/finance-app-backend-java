package com.victorprado.financeapp.entrypoint.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ExceptionResponse {

  private String message;
}
