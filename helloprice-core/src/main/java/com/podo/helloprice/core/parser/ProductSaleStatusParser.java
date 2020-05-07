package com.podo.helloprice.core.parser;

import com.podo.helloprice.core.enums.ProductSaleStatus;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static com.podo.helloprice.core.enums.ProductSaleStatus.*;

@UtilityClass
public class ProductSaleStatusParser {

    private static Map<ProductSaleStatus, String> PRODUCT_SALE_STATUS_TO_KR = new HashMap<>();

    static {
        PRODUCT_SALE_STATUS_TO_KR.put(SALE, "판매중");
        PRODUCT_SALE_STATUS_TO_KR.put(DISCONTINUE, "단종");
        PRODUCT_SALE_STATUS_TO_KR.put(EMPTY_AMOUNT, "일시품절");
        PRODUCT_SALE_STATUS_TO_KR.put(NOT_SUPPORT, "가격비교중지");
        PRODUCT_SALE_STATUS_TO_KR.put(UNKNOWN, "알 수 없음");
    }

    public static String kr(ProductSaleStatus productSaleStatus) {
        return PRODUCT_SALE_STATUS_TO_KR.get(productSaleStatus);
    }

    public static ProductSaleStatus from(String kr) {
        for (ProductSaleStatus productSaleStatus : PRODUCT_SALE_STATUS_TO_KR.keySet()) {
            if (PRODUCT_SALE_STATUS_TO_KR.get(productSaleStatus).equals(kr)) {
                return productSaleStatus;
            }
        }

        return null;
    }

}
