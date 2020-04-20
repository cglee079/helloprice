package com.podo.helloprice.telegram.app.menu.product.typeselect;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductTypeSelectResponse {

    public static String successAddNotifyProduct() {

        return new StringBuilder()
                .append("상품 가격알림이 등록되었습니다\n")
                .append("지금부터 상품의 <b>최저가격</b>이 갱신되면 알림이 전송됩니다!\n")
                .toString();

    }

    public static String alreadySetNotifyProduct(String productName, PriceType priceType) {
        return new StringBuilder()
                .append("'")
                .append(productName)
                .append("/★")
                .append(priceType.kr())
                .append("' 은 이미 알림이 등록된 상품입니다\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String hasMaxProduct() {
        return new StringBuilder()
                .append("죄송합니다, 최대 상품 알림 수를 초과했습니다.")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();

    }

}
