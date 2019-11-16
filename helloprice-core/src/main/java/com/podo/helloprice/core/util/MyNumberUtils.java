package com.podo.helloprice.core.util;

import java.util.Random;

public class MyNumberUtils {

    public static int rand(int size) {
        final long seed = System.currentTimeMillis();

        final Random rand = new Random(seed);

        return rand.nextInt(size);
    }
}
