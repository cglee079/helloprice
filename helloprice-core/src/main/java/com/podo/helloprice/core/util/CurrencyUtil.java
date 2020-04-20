package com.podo.helloprice.core.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.util.Objects;

@UtilityClass
public class CurrencyUtil {

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,###");

    public static String toKRW(Integer krw) {
        if(Objects.isNull(krw)){
            krw = 0;
        }

        CURRENCY_FORMAT.setMinimumFractionDigits(0);
        CURRENCY_FORMAT.setMaximumFractionDigits(0);
        CURRENCY_FORMAT.setPositiveSuffix("원");
        CURRENCY_FORMAT.setNegativeSuffix("원");
        return CURRENCY_FORMAT.format(krw);
    }
}
