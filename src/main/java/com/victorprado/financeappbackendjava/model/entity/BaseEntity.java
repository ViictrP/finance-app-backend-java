package com.victorprado.financeappbackendjava.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity<T extends Serializable> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private T id;
  private LocalDateTime createdAt;
  private LocalDateTime modificatedAt;

  @PrePersist
  public void prePersis() {
    this.createdAt = LocalDateTime.now();
    this.modificatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.modificatedAt = LocalDateTime.now();
  }

  public abstract Boolean validate();
}
