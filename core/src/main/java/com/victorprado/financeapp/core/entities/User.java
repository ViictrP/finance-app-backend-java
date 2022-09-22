package com.victorprado.financeapp.core.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class User extends Entity {

  @NotBlank(message = "Name must be provided")
  private String name;

  @NotBlank(message = "Lastname must be provided")
  private String lastname;

  @Email(message = "Email should be valid")
  @Include
  private String email;

  @NotBlank(message = "Password must be provided")
  private String password;

  private boolean active;
  private LocalDateTime createdAt;
  private BigDecimal salary;

  @Valid
  private List<CreditCard> creditCards;

  @Valid
  private List<Transaction> transactions;
}
