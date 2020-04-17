package com.podo.helloprice.product.update.analysis.processor.notify.executor.helper;

import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import lombok.experimental.UtilityClass;

import static com.podo.helloprice.core.util.CurrencyUtil.toKrw;
import static java.math.BigDecimal.valueOf;

@UtilityClass
public class SaleNotifyHelper {

    public static String title(ProductDetailDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품의 최저가격이  ")
                .append(toKrw(product.getPrice()))
                .append(" 으로 갱신되었습니다.")
                .toString();
    }

    public static String contents(ProductDetailDto product) {
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
                .append("\n")
                .append("---------------------------------------\n")
                .append("\n")
                .append("<b>")
                .append("해당 상품은 최저가가 갱신되었습니다!!\n")
                .append("</b>")
                .append("\n")
                .append(ProductDescribe.descProductDetailWithChangeMessage(product))
                .toString();
    }
}
