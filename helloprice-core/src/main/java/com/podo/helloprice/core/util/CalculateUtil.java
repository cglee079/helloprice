package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

@UtilityClass
public class CalculateUtil {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final BigDecimal PERCENT_OF_100 = valueOf(100);
    private static final BigDecimal PERCENT_OF_MINUS_100 = valueOf(-100);

    public static BigDecimal getChangePercent(Integer x, Integer y) {
        final BigDecimal dx = valueOf(x);
        final BigDecimal dy = valueOf(y);

        if (dx.compareTo(ZERO) == 0 && dy.compareTo(ZERO) == 0) {
            return ZERO;
        }

        if (dy.compareTo(ZERO) == 0) {
            return PERCENT_OF_100;
        }

        if (dx.compareTo(ZERO) == 0) {
            return PERCENT_OF_MINUS_100;
        }

        return dx.subtract(dy).divide(dy, 4, RoundingMode.HALF_UP)
                .multiply(PERCENT_OF_100);
    }

    public static String getSignPercent(Integer x, Integer y) {
        String prefix = "";

        final BigDecimal changePercent = CalculateUtil.getChangePercent(x, y);

        if (changePercent.compareTo(ZERO) > 0) {
            prefix = "+";
        }

        return prefix + DECIMAL_FORMAT.format(changePercent.doubleValue()) + "%";
    }
}
