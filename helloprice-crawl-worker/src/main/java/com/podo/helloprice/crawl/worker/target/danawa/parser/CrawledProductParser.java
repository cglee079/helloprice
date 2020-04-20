package com.podo.helloprice.crawl.worker.target.danawa.parser;

import com.podo.helloprice.core.model.ProductSaleStatus;
import com.podo.helloprice.core.util.StringUtil;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.util.Objects;

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
    public static final String CARD_TYPE_POSTFIX = "카드최저가";

    public CrawledProduct parse(Document document, String productCode, String url, LocalDateTime crawledAt) {
        try {
            final String productName = document.select(SELECTOR_PRODUCT_NAME).text().replace("[다나와]", "").trim();
            final String description = document.select(SELECTOR_PRODUCT_DESC).text().replace("[다나와]", "").trim();
            final String imageUrl = document.select(SELECTOR_PRODUCT_IMAGE).attr("src");

            final Integer price = getPrice(document);
            final Integer cashPrice = getCashPrice(document);
            final String cardType = getCardType(document);
            final Integer cardPrice = getCardPrice(document);

            final ProductSaleStatus saleStatus = getProductSaleStatus(price, document.select(SELECTOR_PRODUCT_SALE_STATUS).text().trim());

            if (StringUtil.isEmpty(productName)) {
                return null;
            }

            return CrawledProduct.builder()
                    .productCode(productCode)
                    .url(url)
                    .productName(productName)
                    .description(description)
                    .imageUrl(imageUrl)
                    .price(price)
                    .cashPrice(cashPrice)
                    .cardType(cardType)
                    .cardPrice(cardPrice)
                    .saleStatus(saleStatus)
                    .crawledAt(crawledAt)
                    .build();

        } catch (RuntimeException e) {
            return null;
        }
    }

    private Integer getPrice(Document document) {
        final Element priceElement = document.select(SELECTOR_PRODUCT_PRICE).first();
        return Objects.isNull(priceElement) ? 0 : Integer.parseInt(priceElement.text().replace(",", ""));
    }

    private Integer getCashPrice(Document document) {
        final Element cashPriceElement = document.select(SELECTOR_PRODUCT_CASH_PRICE).first();
        return Objects.isNull(cashPriceElement) ? null : Integer.parseInt(cashPriceElement.text().replace(",", ""));
    }

    private String getCardType(Document document) {
        final Element cardTypeElement = document.selectFirst(SELECTOR_PRODUCT_CARD_TYPE);
        return Objects.isNull(cardTypeElement) ? null : cardTypeElement.text().replace(CARD_TYPE_POSTFIX, "").trim();
    }

    private Integer getCardPrice(Document document) {
        final Element cardPriceElement = document.select(SELECTOR_PRODUCT_CARD_PRICE).first();
        return Objects.isNull(cardPriceElement) ? null : Integer.parseInt(cardPriceElement.text().replace(",", ""));
    }

    private ProductSaleStatus getProductSaleStatus(Integer productPrice, String saleStatusText) {
        if (productPrice != 0) {
            return ProductSaleStatus.SALE;
        }

        return getProductSaleStatusByCrawledSaleStatusText(saleStatusText);
    }

    private ProductSaleStatus getProductSaleStatusByCrawledSaleStatusText(String saleStatusText) {
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
