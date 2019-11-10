package com.podo.helloprice.core.util;

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
}
