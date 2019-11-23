package com.podo.helloprice.core.util;

import java.util.Random;

public class MyNumberUtils {

    public static int getRandomInt(int size) {
        final Random rand = new Random(System.currentTimeMillis());

        return rand.nextInt(size);
    }

    public static boolean isInteger(String integerString) {
        try {
            Integer.parseInt(integerString);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
