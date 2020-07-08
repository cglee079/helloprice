package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class NumberUtil {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static int getRandomInt(int size) {
        return RANDOM.nextInt(size);
    }

    public static boolean isInteger(String strOfInteger) {
        try {
            Integer.parseInt(strOfInteger);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
