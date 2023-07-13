package com.victorprado.financeappbackendjava.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Where(clause = "deleted = false")
public class Salary extends BaseEntity<Long> {

  @NotBlank(message = "The user ID is required")
  private String userId;

  @Column(precision = 2)
  @NotNull(message = "The value is required")
  private BigDecimal value;

  @NotBlank(message = "The month is required")
  private String month;

  @NotNull(message = "The year is required")
  private Integer year;

  @Override
  public boolean validate() {
    return true;
  }
}
