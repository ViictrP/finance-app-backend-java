package com.victorprado.financeappbackendjava.domain.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum MonthEnum {
  JAN(1),
  FEV(2),
  MAR(3),
  ABR(4),
  MAI(5),
  JUN(6),
  JUL(7),
  AGO(8),
  SET(9),
  OUT(10),
  NOV(11),
  DEZ(12);
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
