package com.podo.helloprice.core.parser;

import com.podo.helloprice.core.enums.SaleType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static com.podo.helloprice.core.enums.SaleType.*;

@UtilityClass
public class SaleTypeParser {

    private static Map<SaleType, String> PRICE_TYPE_TO_KR = new HashMap<>();

    static {
        PRICE_TYPE_TO_KR.put(NORMAL, "일반 최저가");
        PRICE_TYPE_TO_KR.put(CASH, "현금 최저가");
        PRICE_TYPE_TO_KR.put(CARD, "카드 최저가");
    }

    public static String kr(SaleType saleType) {
        return PRICE_TYPE_TO_KR.get(saleType);
    }

    public static SaleType from(String kr) {
        for (SaleType saleType : PRICE_TYPE_TO_KR.keySet()) {
            if (PRICE_TYPE_TO_KR.get(saleType).equals(kr)) {
                return saleType;
            }
        }

        return null;
    }

}
