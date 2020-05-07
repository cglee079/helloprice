package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtil {

    public static Double divide(Integer x, Integer y) {
        if (y == 0) {
            throw new RuntimeException("0으로 나눌 수 없습니다");
        }
        return ((double) x / y);
    }
}
