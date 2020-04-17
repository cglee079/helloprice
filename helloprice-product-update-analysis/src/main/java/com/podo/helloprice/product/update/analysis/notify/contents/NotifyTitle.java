package com.podo.helloprice.product.update.analysis.notify.contents;

import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotifyTitle {

    public static String notifyProductReSale(ProductDetailDto productDetail) {
        return new StringBuilder()
                .append("'")
                .append(productDetail.getProductName())
                .append("' 상품이 ")
                .append(CurrencyUtil.toKrw(productDetail.getPrice()))
                .append(" 으로 다시 판매를 시작했습니다.")
                .toString();
    }

    public static String notifyTooManyDead(int deadCount) {
        return new StringBuilder()
                .append("관리자님 너무 많은 상품페이지를 확인 할 수 없습니다. ")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }

    public static String notifyTooManyUnknown(int deadCount) {
        return new StringBuilder()
                .append("관리자님 너무 많은 상품 상태를 확인 할 수 없습니다.\n")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }


}
