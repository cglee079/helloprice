package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class MathUtil {

    public static Double divide(Integer x, Integer y) {
        if (y == 0) {
            throw new RuntimeException("0으로 나눌 수 없습니다");
        }

        final BigDecimal dx = BigDecimal.valueOf(x);
        final BigDecimal dy = BigDecimal.valueOf(y);

        return dx.divide(dy, 5, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }
}
