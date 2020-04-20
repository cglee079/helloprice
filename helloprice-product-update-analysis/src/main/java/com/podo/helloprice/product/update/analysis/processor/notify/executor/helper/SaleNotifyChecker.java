package com.podo.helloprice.product.update.analysis.processor.notify.executor.helper;

import com.podo.helloprice.core.util.CalculateUtil;
import lombok.experimental.UtilityClass;

import static java.math.BigDecimal.valueOf;

@UtilityClass
public class SaleNotifyChecker {

    public static boolean satisfiedSendNotify(Integer price, Integer beforePrice){
        return price < beforePrice && CalculateUtil.getChangePercent(price, beforePrice).compareTo(valueOf(-1)) < 0;
    }
}
