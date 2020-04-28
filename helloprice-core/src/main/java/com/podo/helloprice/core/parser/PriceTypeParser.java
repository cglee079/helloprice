package com.podo.helloprice.core.parser;

import com.podo.helloprice.core.enums.PriceType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static com.podo.helloprice.core.enums.PriceType.*;

@UtilityClass
public class PriceTypeParser {

    private static Map<PriceType, String> PRICE_TYPE_TO_KR = new HashMap<>();

    static {
        PRICE_TYPE_TO_KR.put(NORMAL, "일반 최저가");
        PRICE_TYPE_TO_KR.put(CASH, "현금 최저가");
        PRICE_TYPE_TO_KR.put(CARD, "카드 최저가");
    }

    public static String kr(PriceType priceType) {
        return PRICE_TYPE_TO_KR.get(priceType);
    }

    public static PriceType from(String kr) {
        for (PriceType priceType : PRICE_TYPE_TO_KR.keySet()) {
            if (PRICE_TYPE_TO_KR.get(priceType).equals(kr)) {
                return priceType;
            }
        }

        return null;
    }

}
