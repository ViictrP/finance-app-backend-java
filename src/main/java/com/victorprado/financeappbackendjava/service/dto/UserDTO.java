package com.victorprado.financeappbackendjava.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDTO {

  private String id;
  private String name;
  private String lastname;
  private String email;
}
