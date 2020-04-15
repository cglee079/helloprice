package com.podo.helloprice.telegram.domain.product.model;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum PriceType {

    NORMAL("일반"), CASH("현금"), CARD("카드");

    private final String value;

    public static PriceType from(String value) {
        return Arrays.stream(PriceType.values())
                .filter(p -> p.value().equals(value))
                .findFirst().orElse(null);
    }

    public String value() {
        return this.value;
    }
}
