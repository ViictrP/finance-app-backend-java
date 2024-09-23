package com.victor.financeapp.backend.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Context {
    CURRENCY("CURRENCY"),
    USDBRL("USDBRL");

    private final String type;
}
