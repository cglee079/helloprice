package com.podo.helloprice.telegram.app.menu.product.global;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductAliveStatus;
import com.podo.helloprice.core.model.ProductSaleStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.telegram.domain.product.dto.ProductOneMorePriceTypeDto;
import com.podo.helloprice.telegram.domain.product.dto.ProductOneMorePriceTypeDto.Price;
import com.podo.helloprice.telegram.domain.product.dto.ProductOnePriceTypeDto;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

import static com.podo.helloprice.core.util.CurrencyUtil.toKRW;
import static com.podo.helloprice.core.util.DateTimeUtil.dateTimeToString;

@UtilityClass
public class ProductCommonResponse {

    public static final String DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";

    public static String descProductOneMoreTypeDetail(ProductOneMorePriceTypeDto product) {
        final StringBuilder message = new StringBuilder();

        final Map<PriceType, Price> prices = product.getPrices();

        message.append("<b>")
                .append("최종확인시간 : ")
                .append(dateTimeToString(product.getLastCrawledAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("<b>상품링크</b> : ")
                .append(product.getUrl())
                .append("\n")

                .append("<b>상품코드</b> : ")
                .append(product.getProductCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(product.getSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품가격/일반: ")
                .append(toKRW(prices.get(PriceType.NORMAL).getPrice()))
                .append("</b>")
                .append("\n");

        if (prices.containsKey(PriceType.CASH)) {
            message.append("<b>")
                    .append("상품가격/현금: ")
                    .append(toKRW(prices.get(PriceType.CASH).getPrice()))
                    .append("</b>")
                    .append("\n");
        }

        if (prices.containsKey(PriceType.CARD)) {
            message.append("<b>")
                    .append("상품가격/카드: ")
                    .append(toKRW(prices.get(PriceType.CARD).getPrice()))
                    .append("(" + prices.get(PriceType.CARD).getAdditionalInfo() + ")")
                    .append("</b>")
                    .append("\n");
        }

        return message.toString();
    }

    public static String descProductOneTypeDetail(ProductOnePriceTypeDto product) {
        final Integer productPrice = product.getPrice();
        final Integer productBeforePrice = product.getBeforePrice();

        return new StringBuilder().append("<b>")
                .append("최종확인시간 : ")
                .append(dateTimeToString(product.getLastCrawledAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("<b>가격변동시간</b> : ")
                .append(dateTimeToString(product.getLastUpdateAt(), DATE_TIME_FORMAT))
                .append("\n")
                .append("\n")

                .append("<b>상품링크</b> : ")
                .append(product.getUrl())
                .append("\n")

                .append("<b>상품코드</b> : ")
                .append(product.getProductCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(product.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격타입 : ")
                .append("★")
                .append(product.getPriceType().kr())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(product.getSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("이전가격 : ")
                .append(toKRW(productBeforePrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(toKRW(productPrice))
                .append(StringUtils.isEmpty(product.getPriceAdditionalInfo()) ? "" : "(" + product.getPriceAdditionalInfo() + ")")
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CalculateUtil.getPercentStringWithPlusMinusSign(productPrice, productBeforePrice))
                .append("</b>")
                .toString();
    }

    public static String descProductWithChangeMessage(ProductOnePriceTypeDto product) {
        return new StringBuilder()
                .append(descProductOneTypeDetail(product))
                .append("\n")
                .append("\n")
                .append(descProductChange(product))
                .toString();
    }

    public static String descProductChange(ProductOnePriceTypeDto product) {
        if (product.getAliveStatus().equals(ProductAliveStatus.DEAD)) {
            return "죄송합니다.. <b>상품의 페이지를 확인 할 수 없어요..</b>";
        }

        return descChangeBySaleStatus(product);
    }

    private static String descChangeBySaleStatus(ProductOnePriceTypeDto product) {

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
                return descSaleStatusChange(product.getPrice(), product.getBeforePrice(), product.getPriceType());
        }

        return "";
    }

    private static String descSaleStatusChange(Integer productPrice, Integer productBeforePrice, PriceType priceType) {
        if (productPrice.equals(0)) {
            return "<i> '" + priceType.kr() + "' 타입의 최저가는 더이상 진행되지 않아요. :'( <i>";
        }

        if (productPrice > 0 && productBeforePrice.equals(0)) {
            return new StringBuilder().append("야호!  <b>")
                    .append(toKRW(productPrice))
                    .append("</b>으로 다시 판매를 시작했어요!!")
                    .toString();
        }

        if (productPrice > productBeforePrice) {
            return new StringBuilder().append("죄송합니다.. 가격이 <b>")
                    .append(toKRW(productPrice - productBeforePrice))
                    .append("</b> 올랐어요...")
                    .toString();
        }

        if (productPrice < productBeforePrice) {
            return new StringBuilder().append("야호! 가격이 <b>")
                    .append(toKRW(productBeforePrice - productPrice))
                    .append("</b> 떨어졌어요!!")
                    .toString();
        }

        return "<i>아직 가격이 똑같아요! 좀만 더 기다려보세요!</i>";
    }

}
