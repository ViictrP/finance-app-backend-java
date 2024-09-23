package com.victor.financeapp.backend.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MonthEnum {
  JAN(1),
  FEB(2),
  MAR(3),
  APR(4),
  MAY(5),
  JUN(6),
  JUL(7),
  AUG(8),
  SEP(9),
  OCT(10),
  NOV(11),
  DEC(12);
  private final Integer index;

  MonthEnum(Integer index) {
    this.index = index;
  }

  public static MonthEnum getMonth(Integer index) {
    return Arrays.stream(MonthEnum.values())
      .filter(month -> index.equals(month.getIndex()))
      .findFirst().orElseThrow();
  }

  public static MonthEnum getMonth(String name) {
    return Arrays.stream(MonthEnum.values())
      .filter(month -> name.equals(month.name()))
      .findFirst().orElseThrow();
  }
}
