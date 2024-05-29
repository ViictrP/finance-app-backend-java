package com.victorprado.financeappbackendjava.domain.enums;

import lombok.Getter;

@Getter
public enum UserProperty {
    MONTH_CLOSURE_DAY("MONTH_CLOSURE_DAY"),
    CURRENCY_CONVERSION("CURRENCY_CONVERSION"),
    CURRENCY_CONVERSION_TYPE("CURRENCY_CONVERSION_TYPE"),
    CURRENCY("CURRENCY"),
    CURRENCY_CONVERSION_TAX("CURRENCY_CONVERSION_TAX"),
    SALARY_TAX("SALARY_TAX"),
    DOLLAR_COTATION("DOLLAR_COTATION");

    private final String name;

    UserProperty(String name) {
        this.name = name;
    }
}
