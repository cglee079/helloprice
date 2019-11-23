package com.podo.helloprice.core.util;

        import java.text.DecimalFormat;

public class MyCurrencyUtils {

    private static final DecimalFormat df = new DecimalFormat("#,###");

    public static String toKrw(double krw) {
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(0);
        df.setPositiveSuffix("원");
        df.setNegativeSuffix("원");
        return df.format(krw);
    }
}
