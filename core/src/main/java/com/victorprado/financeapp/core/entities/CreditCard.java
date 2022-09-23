package com.victorprado.financeapp.core.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class CreditCard extends Entity {

  @Include
  private String number;
}
