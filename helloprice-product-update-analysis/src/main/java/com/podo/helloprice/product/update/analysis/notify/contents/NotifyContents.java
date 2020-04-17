package com.podo.helloprice.product.update.analysis.notify.contents;

import com.podo.helloprice.product.update.analysis.processor.notify.executor.helper.ProductDescribe;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotifyContents {

    public static String notifyProductResale(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 다시 판매를 시작했습니다!\n")
                .append("</b>")
                .append("\n")
                .append(ProductDescribe.descProductDetailWithChangeMessage(product))
                .toString();
    }

    public static String notifyTooManyDead(int deadCount) {
        return new StringBuilder()
                .append("<b>")
                .append("관리자님 너무 많은 상품페이지를 확인 할 수 없습니다.\n")
                .append("</b>")
                .append("\n")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }

    public static String notifyTooManyUnknown(int deadCount) {
        return new StringBuilder()
                .append("<b>")
                .append("관리자님 너무 많은 상품 상태를 확인 할 수 없습니다.\n")
                .append("</b>")
                .append("\n")
                .append("DEAD COUNT : ")
                .append(deadCount)
                .toString();
    }


}
