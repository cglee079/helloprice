package com.podo.helloprice.core.util;

import java.text.DecimalFormat;

public class MyCalculateUtils {

    public static Double getChangePercent(double a, double b) {

        double percent;

        if (b == 0) {
            percent = 100;
        } else if (a == 0) {
            percent = -100;
        } else {
            double gap = a - b;
            percent = (gap / b) * 100;
        }

        return percent;
    }

    public static String getPercentStringWithPlusMinusSign(double a, double b) {
        String prefix = "";

        double percent = MyCalculateUtils.getChangePercent(a, b);

        if (percent > 0) {
            prefix = "+";
        }

        DecimalFormat df = new DecimalFormat("#.##");
        return prefix + df.format(percent) + "%";
    }
}
