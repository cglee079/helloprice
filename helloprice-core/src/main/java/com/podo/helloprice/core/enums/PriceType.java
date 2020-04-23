package com.podo.helloprice.core.enums;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum PriceType {

    NORMAL("일반 최저가"), CASH("현금 최저가"), CARD("카드 최저가");

    private final String kr;

    public static PriceType from(String kr) {
        return Arrays.stream(PriceType.values())
                .filter(p -> p.kr().equals(kr))
                .findFirst().orElse(null);
    }

    public String kr() {
        return this.kr;
    }
}
