package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class StringUtil {

    public static boolean isEmpty(String str) {
        return Objects.isNull(str) || str.trim().isEmpty();
    }

    public static String summary(String str, int endIndex) {
        return str.replace("\n", "")
                .replace("\r", "")
                .substring(0, endIndex);
    }
}
