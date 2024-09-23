package com.victor.financeapp.backend.service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BackupDTO {
  @Valid
  @NotEmpty(message = "The user's information is required")
  List<UserDTO> users;
}
