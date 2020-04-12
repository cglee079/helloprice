package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.math.BigDecimal.ZERO;

@UtilityClass
public class CalculateUtil {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static BigDecimal getChangePercent(Integer x, Integer y) {
        final BigDecimal ax = BigDecimal.valueOf(x);
        final BigDecimal ay = BigDecimal.valueOf(y);

        if (ax.compareTo(ZERO) == 0 && ay.compareTo(ZERO) == 0) {
            return ZERO;
        }

        if (ay.compareTo(ZERO) == 0) {
            return BigDecimal.valueOf(100);
        }

        if (ax.compareTo(ZERO) == 0) {
            return BigDecimal.valueOf(-100);
        }

        return ax.subtract(ay).divide(ay, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public static String getPercentStringWithPlusMinusSign(Integer a, Integer b) {
        String prefix = "";

        final BigDecimal changePercent = CalculateUtil.getChangePercent(a, b);

        if (changePercent.compareTo(ZERO) > 0) {
            prefix = "+";
        }

        return prefix + DECIMAL_FORMAT.format(changePercent.doubleValue()) + "%";
    }
}
