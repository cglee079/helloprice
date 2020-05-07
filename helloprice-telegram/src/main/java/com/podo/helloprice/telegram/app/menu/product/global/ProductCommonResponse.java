package com.podo.helloprice.telegram.app.menu.product.global;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.parser.SaleTypeParser;
import com.podo.helloprice.core.parser.ProductSaleStatusParser;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.telegram.domain.product.dto.ProductDto;
import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import static com.podo.helloprice.core.util.CurrencyUtil.toKRW;
import static com.podo.helloprice.core.util.DateTimeUtil.dateTimeToString;

@UtilityClass
public class ProductCommonResponse {

    private static final String DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";

    public static String descProductWithChangeMessage(ProductSaleDto productSale) {
        return new StringBuilder()
                .append(descProductDetail(productSale))
                .append("\n")
                .append("\n")
                .append(descProductChange(productSale))
                .toString();
    }

    private static String descProductDetail(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();
        final Integer price = productSale.getPrice();
        final Integer prevPrice = productSale.getPrevPrice();

        return new StringBuilder()
                .append("<b>")
                .append("최종확인시간 : ")
                .append(dateTimeToString(product.getLastCrawledAt(), DATE_TIME_FORMAT))
                .append("</b>")
                .append("\n")

                .append("<b>가격변동시간</b> : ")
                .append(dateTimeToString(productSale.getLastUpdateAt(), DATE_TIME_FORMAT))
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
                .append(SaleTypeParser.kr(productSale.getSaleType()))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(ProductSaleStatusParser.kr(product.getSaleStatus()))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("이전가격 : ")
                .append(toKRW(prevPrice))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("현재가격 : ")
                .append(toKRW(price))
                .append(StringUtils.isEmpty(productSale.getAdditionalInfo()) ? "" : "(" + productSale.getAdditionalInfo() + ")")
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("가격변화 : ")
                .append(CalculateUtil.getSignPercent(price, prevPrice))
                .append("</b>")
                .toString();
    }

    private static String descProductChange(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

        if (product.getAliveStatus().equals(ProductAliveStatus.DEAD)) {
            return "죄송합니다.. <b>상품의 페이지를 확인 할 수 없어요..</b>";
        }

        return descChangeBySaleStatus(productSale);
    }

    private static String descChangeBySaleStatus(ProductSaleDto productSale) {
        final ProductDto product = productSale.getProduct();

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
                return descSaleStatusChange(productSale.getPrice(), productSale.getPrevPrice(), productSale.getSaleType());
        }

        return "";
    }

    private static String descSaleStatusChange(Integer price, Integer prevPrice, SaleType saleType) {

        if (price.equals(0)) {
            return "<i>죄송합니다, <b>" + SaleTypeParser.kr(saleType) + "</b>는 판매가 진행되지 않고 있어요..</i>";
        }

        if (price > 0 && prevPrice.equals(0)) {
            return new StringBuilder().append("야호! <b>")
                    .append(toKRW(price))
                    .append("</b>으로 다시 판매를 시작했어요!!")
                    .toString();
        }

        if (price > prevPrice) {
            return new StringBuilder().append("죄송합니다.. 가격이 <b>")
                    .append(toKRW(price - prevPrice))
                    .append("</b> 올랐어요...")
                    .toString();
        }

        if (price < prevPrice) {
            return new StringBuilder().append("야호! 가격이 <b>")
                    .append(toKRW(prevPrice - price))
                    .append("</b> 떨어졌어요!!")
                    .toString();
        }

        return "<i>아직 가격이 똑같아요! 좀만 더 기다려보세요!</i>";
    }

}
