package com.podo.helloprice.core.util;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyFormatUtils {

    public static String dateTimeToString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

        return dateTimeFormatter.format(localDateTime);
    }

    public static String toSignPercentStr(double a, double b) {
        String prefix = "";

        double percent = MyCalculateUtils.getChangePercent(a, b);

        if (percent > 0) {
            prefix = "+";
        }

        DecimalFormat df = new DecimalFormat("#.##");
        return prefix + df.format(percent) + "%";
    }
}
