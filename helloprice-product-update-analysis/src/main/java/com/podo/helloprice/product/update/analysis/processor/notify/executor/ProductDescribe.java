package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.core.util.DateTimeUtil;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.core.util.CurrencyUtil;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

@UtilityClass
public class ProductDescribe {

    private static final String DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";

    public static String descProductDetailWithChangeMessage(ProductDetailDto product) {
        return new StringBuilder()
                .append(descProductDetail(product))
                .append("\n")
                .append("\n")
                .append(descProductChange(product))
                .toString();
    }

    private static String descProductDetail(ProductDetailDto product) {

        final StringBuilder message = new StringBuilder();
        final Integer price = product.getPrice();
        final Integer prevPrice = product.getPrevPrice();

        message.append("<b>")
                .append("최종확인시간 : ")
                .append(DateTimeUtil.dateTimeToString(product.getLastCrawledAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("<b>가격변동시간</b> : ")
                .append(DateTimeUtil.dateTimeToString(product.getLastUpdateAt(), DATE_TIME_FORMAT))
                .append("\n")
                .append("\n")

                .append("<b>상품코드</b> : ")
                .append(product.getProductCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>상품링크</b> : ")
                .append(product.getUrl())
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(product.getSaleStatus().kr())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격타입 : ★")
                .append(product.getPriceType().kr())
                .append(StringUtils.isEmpty(product.getPriceAdditionalInfo()) ? "" : "(" + product.getPriceAdditionalInfo() + ")")
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("이전가격 : ")
                .append(CurrencyUtil.toKRW(prevPrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(CurrencyUtil.toKRW(price))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CalculateUtil.getSignPercent(price, prevPrice))
                .append("</b>");

        return message.toString();
    }


    private static String descProductChange(ProductDetailDto product) {
        if (product.getAliveStatus().equals(ProductAliveStatus.DEAD)) {
            return "죄송합니다.. <b>상품의 페이지를 확인 할 수 없어요..</b>";
        }

        return descChangeBySaleStatus(product);
    }

    private static String descChangeBySaleStatus(ProductDetailDto product) {

        final ProductSaleStatus saleStatus = product.getSaleStatus();
        switch (saleStatus) {
            case UNKNOWN:
                return "죄송합니다.. <b>상품의 상태를 알 수 없어요..</b>";
            case DISCONTINUE:
                return "죄송합니다.. <b>상품이 단종 됬어요..</b>";
            case NOT_SUPPORT:
                return "죄송합니다.. <b>상품의 가격비교가 중지되었어요..</b>";
            case EMPTY_AMOUNT:
                return "죄송합니다.. <b>상품의 재고가 없어요..</b>";
            case SALE:
                return descSaleStatusChange(product.getPrice(), product.getPrevPrice(), product.getPriceType());
        }

        return "";
    }

    private static String descSaleStatusChange(Integer price, Integer prevPrice, PriceType priceType) {
        final StringBuilder message = new StringBuilder();

        if (price.equals(0)) {
            return "<i>죄송합니다, <b>" + priceType.kr() + "</b>는 판매가 진행되지 않고 있어요..</i>";
        }

        if (prevPrice.equals(0)) {
            message.append("야호!  <b>")
                    .append(CurrencyUtil.toKRW(price))
                    .append("</b>으로 다시 판매를 시작했어요!!");
            return message.toString();
        }

        if (price > prevPrice) {
            message.append("죄송합니다.. 가격이 <b>")
                    .append(CurrencyUtil.toKRW(price - prevPrice))
                    .append("</b> 올랐어요...");
            return message.toString();
        }

        if (price.equals(prevPrice)) {
            message.append("<i>아직 가격이 똑같아요! 좀만 더 기다려보세요!</i>");
            return message.toString();
        }

        message.append("야호! 가격이 <b>")
                .append(CurrencyUtil.toKRW(prevPrice - price))
                .append("</b> 떨어졌어요!!");

        return message.toString();
    }


}
