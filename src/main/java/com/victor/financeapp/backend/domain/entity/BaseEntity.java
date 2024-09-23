package com.victor.financeapp.backend.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Where(clause = "deleted = false")
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private T id;
  private LocalDateTime createdAt;
  private LocalDateTime modificatedAt;
  private LocalDateTime deleteDate;
  private boolean deleted = false;

  public boolean isNew() {
    return this.id == null;
  }

  @PrePersist
  public void prePersis() {
    this.createdAt = LocalDateTime.now();
    this.modificatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.modificatedAt = LocalDateTime.now();
    if (this.deleted) {
      this.deleteDate = LocalDateTime.now();
    }
  }

  public abstract boolean validate();
}
