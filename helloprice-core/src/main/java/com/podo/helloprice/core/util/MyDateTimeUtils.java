package com.podo.helloprice.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDateTimeUtils {

    public static String dateTimeToString(LocalDateTime localDateTime, String format) {
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

}
