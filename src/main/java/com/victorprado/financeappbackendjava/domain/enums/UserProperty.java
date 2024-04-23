package com.victorprado.financeappbackendjava.domain.enums;

import lombok.Getter;

@Getter
public enum UserProperty {
    MONTH_CLOSURE_DAY("MONTH_CLOSURE_DAY");

    private final String name;

    UserProperty(String name) {
        this.name = name;
    }
}
