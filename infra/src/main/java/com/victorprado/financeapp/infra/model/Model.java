package com.victorprado.financeapp.infra.model;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Model {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  protected LocalDateTime createdAt = LocalDateTime.now();
}
