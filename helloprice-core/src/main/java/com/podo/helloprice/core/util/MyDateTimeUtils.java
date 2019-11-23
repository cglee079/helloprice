package com.podo.helloprice.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDateTimeUtils {

    public static String dateTimeToString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

        return dateTimeFormatter.format(localDateTime);
    }

}
