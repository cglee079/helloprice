package com.podo.helloprice.core.util;

        import java.text.DecimalFormat;

public class MyCurrencyUtils {

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,###");

    public static String toKrw(double krw) {
        CURRENCY_FORMAT.setMinimumFractionDigits(0);
        CURRENCY_FORMAT.setMaximumFractionDigits(0);
        CURRENCY_FORMAT.setPositiveSuffix("원");
        CURRENCY_FORMAT.setNegativeSuffix("원");
        return CURRENCY_FORMAT.format(krw);
    }
}
