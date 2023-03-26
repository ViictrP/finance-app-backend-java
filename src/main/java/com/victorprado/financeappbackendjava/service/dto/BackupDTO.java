package com.victorprado.financeappbackendjava.service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class BackupDTO {
  @Valid
  @NotEmpty(message = "The user's information is required")
  List<UserDTO> users;
}
