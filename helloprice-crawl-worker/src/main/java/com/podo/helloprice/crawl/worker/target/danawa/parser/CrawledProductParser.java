package com.podo.helloprice.crawl.worker.target.danawa.parser;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.util.StringUtil;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.crawl.worker.value.CrawledProduct.CrawledProductPrice;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

import static com.podo.helloprice.core.enums.PriceType.*;

@Slf4j
public class CrawledProductParser {

    private static final String SELECTOR_PRODUCT_NAME = "#blog_content > div.summary_info > div.top_summary > h3";
    private static final String SELECTOR_PRODUCT_DESC = "#blog_content .spec_list";
    private static final String SELECTOR_PRODUCT_PRICE = ".lowest_price > span.lwst_prc > a > em";
    private static final String SELECTOR_PRODUCT_CASH_PRICE = "#lowPriceCash > span.lwst_prc > a > em";
    private static final String SELECTOR_PRODUCT_CARD_TYPE = ".card_list > tr > td.price > a > span.txt_dsc";
    private static final String SELECTOR_PRODUCT_CARD_PRICE = ".card_list > tr > td.price > a > span.txt_prc > em";
    private static final String SELECTOR_PRODUCT_IMAGE = "#baseImage";
    private static final String SELECTOR_PRODUCT_SALE_STATUS = "div.lowest_area > div.no_data > p > strong";

    private static final String TEXT_PRODUCT_STATUS_DISCONTINUE = "단종 된";
    private static final String TEXT_PRODUCT_STATUS_EMPTY_ACCOUNT = "일시 품절";
    private static final String TEXT_PRODUCT_STATUS_NO_SUPPORT = "가격비교 중지";
    private static final String CARD_TYPE_POSTFIX = "카드최저가";

    public CrawledProduct parse(Document document, String productCode, String url, LocalDateTime crawledAt) {
        try {
            final String productName = document.select(SELECTOR_PRODUCT_NAME).text().replace("[다나와]", "").trim();
            final String description = document.select(SELECTOR_PRODUCT_DESC).text().replace("[다나와]", "").trim();
            final String imageUrl = document.select(SELECTOR_PRODUCT_IMAGE).attr("src");


            if (StringUtil.isEmpty(productName)) {
                return null;
            }

            final HashMap<PriceType, CrawledProductPrice> priceTypeToPrice = new HashMap<>();
            priceTypeToPrice.put(NORMAL,  getNormalPrice(document));
            priceTypeToPrice.put(CASH,  getCashPrice(document));
            priceTypeToPrice.put(CARD,  getCardPrice(document));

            final ProductSaleStatus saleStatus = getProductSaleStatus(priceTypeToPrice.get(NORMAL), document.select(SELECTOR_PRODUCT_SALE_STATUS).text().trim());

            return CrawledProduct.builder()
                    .productCode(productCode)
                    .url(url)
                    .productName(productName)
                    .description(description)
                    .imageUrl(imageUrl)
                    .priceTypeToPrice(priceTypeToPrice)
                    .saleStatus(saleStatus)
                    .crawledAt(crawledAt)
                    .build();

        } catch (RuntimeException e) {
            log.error("CRAWL :: ERROR :: 상품정보를 확인 할 수 없습니다.", e);
            return null;
        }
    }

    private CrawledProductPrice getNormalPrice(Document document) {
        final Element priceElement = document.select(SELECTOR_PRODUCT_PRICE).first();
        return Objects.isNull(priceElement) ? new CrawledProductPrice(0) : new CrawledProductPrice(Integer.parseInt(priceElement.text().replace(",", "")));
    }

    private CrawledProductPrice getCashPrice(Document document) {
        final Element cashPriceElement = document.select(SELECTOR_PRODUCT_CASH_PRICE).first();
        return Objects.isNull(cashPriceElement) ? null : new CrawledProductPrice(Integer.parseInt(cashPriceElement.text().replace(",", "")));
    }

    private CrawledProductPrice getCardPrice(Document document) {
        final Element cardPriceElement = document.select(SELECTOR_PRODUCT_CARD_PRICE).first();
        final Element cardTypeElement = document.selectFirst(SELECTOR_PRODUCT_CARD_TYPE);

        if (Objects.isNull(cardTypeElement)) {
            return null;
        }

        return new CrawledProductPrice(
                Integer.parseInt(cardPriceElement.text().replace(",", ""))
                , cardTypeElement.text().replace(CARD_TYPE_POSTFIX, "").trim()
        );
    }

    private ProductSaleStatus getProductSaleStatus(CrawledProductPrice crawledProductPrice, String saleStatusText) {
        if (crawledProductPrice.getPrice() != 0) {
            return ProductSaleStatus.SALE;
        }

        switch (saleStatusText) {
            case TEXT_PRODUCT_STATUS_DISCONTINUE:
                return ProductSaleStatus.DISCONTINUE;
            case TEXT_PRODUCT_STATUS_EMPTY_ACCOUNT:
                return ProductSaleStatus.EMPTY_AMOUNT;
            case TEXT_PRODUCT_STATUS_NO_SUPPORT:
                return ProductSaleStatus.NOT_SUPPORT;
        }

        return ProductSaleStatus.UNKNOWN;
    }

}
