package com.podo.helloprice.telegram.app.menu.product.global;

import com.podo.helloprice.core.parser.ProductSaleStatusParser;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.crawl.worker.value.CrawledProductPrice;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static com.podo.helloprice.core.enums.SaleType.*;

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

                .append(CommonResponse.toHome())
                .toString();
    }

    public static String isDiscontinueProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(ProductAddResponse.descCrawledProduct(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 단종된 상품입니다")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String isErrorProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(ProductAddResponse.descCrawledProduct(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 알 수 없는 상태의 상품입니다")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String isNotSupportProduct(CrawledProduct crawledProduct) {
        return new StringBuilder()
                .append(ProductAddResponse.descCrawledProduct(crawledProduct))
                .append("\n")
                .append("죄송합니다, 페이지에서 가격비교를 제공하지 않는 상품입니다")
                .append("\n")
                .append("\n")
                .append(CommonResponse.toHome())
                .toString();
    }

    public static String descCrawledProduct(CrawledProduct crawledProduct) {
        final CrawledProductPrice normalCrawledProductPrice = crawledProduct.getProductPriceByType(NORMAL);
        final CrawledProductPrice cashCrawledProductPrice = crawledProduct.getProductPriceByType(CASH);
        final CrawledProductPrice cardCrawledProductPrice = crawledProduct.getProductPriceByType(CARD);

        final StringBuilder message = new StringBuilder()
                .append("<b>상품코드</b> : ")
                .append(crawledProduct.getProductCode())
                .append("\n")

                .append("<b>")
                .append("상품이름 : ")
                .append(crawledProduct.getProductName())
                .append("</b>")
                .append("\n")

                .append("상품링크 : ")
                .append(crawledProduct.getUrl())
                .append("\n")

                .append("<b>상품설명</b> : ")
                .append("<i>")
                .append(crawledProduct.getDescription())
                .append("</i>")
                .append("\n")

                .append("<b>")
                .append("상품상태 : ")
                .append(ProductSaleStatusParser.kr(crawledProduct.getSaleStatus()))
                .append("</b>")
                .append("\n")

                .append("<b>")
                .append("상품가격/일반: ")
                .append(CurrencyUtil.toKRW(normalCrawledProductPrice.getPrice()))
                .append("</b>")
                .append("\n");

        if(Objects.nonNull(cashCrawledProductPrice)) {
            message.append("<b>")
                    .append("상품가격/현금: ")
                    .append(CurrencyUtil.toKRW(cashCrawledProductPrice.getPrice()))
                    .append("</b>")
                    .append("\n");
        }

        if(Objects.nonNull(cardCrawledProductPrice)) {
            message.append("<b>")
                    .append("상품가격/카드: ")
                    .append(CurrencyUtil.toKRW(cardCrawledProductPrice.getPrice()))
                    .append("(")
                    .append(cardCrawledProductPrice.getAdditionalInfo())
                    .append(")")
                    .append("</b>")
                    .append("\n");
        }

        return message.toString();
    }
}
