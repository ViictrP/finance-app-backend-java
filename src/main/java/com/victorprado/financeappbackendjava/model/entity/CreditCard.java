package com.victorprado.financeappbackendjava.model.entity;

import jakarta.persistence.Entity;

@Entity
public class CreditCard extends BaseEntity<Long> {

  @Override
  public Boolean validate() {
    return null;
  }
}
