package com.podo.helloprice.core.util;

import java.text.DecimalFormat;

public class MyCalculateUtils {

    public static Double getChangePercent(double a, double b) {

        if (Double.compare(a, 0) == 0 && Double.compare(b, 0) == 0) {
            return 0.0;
        }

        if (Double.compare(b, 0) == 0) {
            return 100.0;
        }

        if (Double.compare(a, 0) == 0) {
            return -100.0;
        }

        double gap = a - b;
        return (gap / b) * 100;
    }

    public static String getPercentStringWithPlusMinusSign(double a, double b) {
        String prefix = "";

        double percent = MyCalculateUtils.getChangePercent(a, b);

        if (percent > 0) {
            prefix = "+";
        }

        return prefix + new DecimalFormat("#.##").format(percent) + "%";
    }
}
