package com.victorprado.financeapp.core.entities;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = "createdAt")
public abstract class Entity {

  private String id;
  private LocalDateTime createdAt;

  public void audit() {
    this.createdAt = LocalDateTime.now();
  }
}
