package com.podo.helloprice.telegram.app.menu.product;

import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.core.util.CurrencyUtil;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class ProductAddResponse {

    public static String wrongProductCode(String productCode) {
        return new StringBuilder()
                .append("죄송합니다, 상품 정보를 가져 올 수 없습니다\n")

                .append("\n")
                .append("상품코드 : ")
                .append(productCode)
                .append("\n")
                .append("\n")
                .append(ProductCommonResponse.toHome())
                .toString();
    }

    public static String isDiscontinueProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(ProductAddResponse.descCrawledProduct(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 단종된 상품입니다")
                .append("\n")
                .append(ProductCommonResponse.toHome())
                .toString();
    }

    public static String isErrorProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(ProductAddResponse.descCrawledProduct(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 알 수 없는 상태의 상품입니다")
                .append("\n")
                .append("\n")
                .append(ProductCommonResponse.toHome())
                .toString();
    }

    public static String isNotSupportProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(ProductAddResponse.descCrawledProduct(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 가격비교를 제공하지 않는 상품입니다")
                .append("\n")
                .append("\n")
                .append(ProductCommonResponse.toHome())
                .toString();
    }

    public static String hasMaxProduct() {
        return new StringBuilder()
                .append("죄송합니다, 최대 상품 알림 수를 초과했습니다.")
                .append("\n")
                .append("\n")
                .append(ProductCommonResponse.toHome())
                .toString();

    }

    public static String descCrawledProduct(CrawledProduct crawledProduct) {
        final StringBuilder message = new StringBuilder()
                .append("<b>상품코드</b> : ")
                .append(crawledProduct.getProductCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(crawledProduct.getProductName())
                .append("</b>")
                .append("\n")

                .append("<b>상품설명</b> : ")
                .append("<i>")
                .append(crawledProduct.getDescription())
                .append("</i>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(crawledProduct.getSaleStatus().getValue())
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품가격/일반: ")
                .append(CurrencyUtil.toKrw(crawledProduct.getPrice()))
                .append("</b>")
                .append("\n");

        if(Objects.nonNull(crawledProduct.getCashPrice())) {
            message.append("<b>")
                    .append("상품가격/현금: ")
                    .append(CurrencyUtil.toKrw(crawledProduct.getCashPrice()))
                    .append("</b>")
                    .append("\n");
        }

        if(Objects.nonNull(crawledProduct.getCardPrice())) {
            message.append("<b>")
                    .append("상품가격/카드: ")
                    .append(CurrencyUtil.toKrw(crawledProduct.getCardPrice()))
                    .append("(" + crawledProduct.getCardType() + ")")
                    .append("</b>")
                    .append("\n");
        }

        return message.toString();
    }
}
