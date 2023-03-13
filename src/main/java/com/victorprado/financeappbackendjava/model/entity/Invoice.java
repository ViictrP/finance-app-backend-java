package com.victorprado.financeappbackendjava.model.entity;

import jakarta.persistence.Entity;

@Entity
public class Invoice extends BaseEntity<Long> {

  @Override
  public Boolean validate() {
    return null;
  }
}
