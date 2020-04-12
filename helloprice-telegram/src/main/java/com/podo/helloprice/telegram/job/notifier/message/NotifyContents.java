package com.podo.helloprice.telegram.job.notifier.message;

import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotifyContents {


    public static String notifyProductDiscontinued(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '단종' 상태로 변경되었습니다\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descProductDetailWithChangeMessage(product))
                .toString();
    }

    public static String notifyProductUnknown(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '알 수 없는' 상태로 변경되었습니다\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descProductDetailWithChangeMessage(product))
                .toString();
    }

    public static String notifyProductEmptyAccount(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '재고없음' 상태로 변경되었습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descProductDetailWithChangeMessage(product))
                .toString();
    }

    public static String notifyProductNotSupport(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '가격비교중비' 상태로 변경되었습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descProductDetailWithChangeMessage(product))
                .toString();
    }


    public static String notifyProductSale(ProductDetailDto product) {
        return new StringBuilder()
                .append(descSummary(product))
                .append("\n")
                .append("---------------------------------------\n")
                .append("\n")
                .append("<b>")
                .append("해당 상품은 최저가가 갱신되었습니다!!\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descProductDetailWithChangeMessage(product))
                .toString();
    }

    private static String descSummary(ProductDetailDto product) {
        final Integer price = product.getPrice();
        final Integer beforePrice = product.getBeforePrice();

        return new StringBuilder()
                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(CurrencyUtil.toKrw(price))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CalculateUtil.getPercentStringWithPlusMinusSign(price, beforePrice))
                .append("</b>")
                .toString();
    }

    public static String notifyProductResale(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 다시 판매를 시작했습니다!\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descProductDetailWithChangeMessage(product))
                .toString();
    }

    public static String notifyProductDead(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품의 상품페이지를 더이상 확인 할 수 없습니다.\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(CommonResponse.descProductDetailWithChangeMessage(product))
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
