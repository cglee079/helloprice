package com.podo.helloprice.crawl.worker.target.danawa.parser;

import com.podo.helloprice.code.model.ProductSaleStatus;
import com.podo.helloprice.core.util.StringUtil;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.podo.helloprice.code.model.ProductSaleStatus.*;

@Slf4j
public class CrawledProductParser {

    private static final String SELECTOR_PRODUCT_NAME = "#blog_content > div.summary_info > div.top_summary > h3";
    private static final String SELECTOR_PRODUCT_DESC = "#blog_content .spec_list";
    private static final String SELECTOR_PRODUCT_PRICE = "span.lwst_prc > a > em";
    private static final String SELECTOR_PRODUCT_IMAGE = "#baseImage";
    private static final String SELECTOR_PRODUCT_SALE_STATUS = "div.lowest_area > div.no_data > p > strong";

    private static final String TEXT_PRODUCT_STATUS_DISCONTINUE = "단종 된";
    private static final String TEXT_PRODUCT_STATUS_EMPTY_ACCOUNT = "일시 품절";
    private static final String TEXT_PRODUCT_STATUS_NO_SUPPORT = "가격비교 중지";
    
    public CrawledProduct parse(Document document, String productCode, String url, LocalDateTime crawledAt) {
        try {
            final String productName = document.select(SELECTOR_PRODUCT_NAME).text().replace("[다나와]", "").trim();
            final String description = document.select(SELECTOR_PRODUCT_DESC).text().replace("[다나와]", "").trim();
            final String imageUrl = document.select(SELECTOR_PRODUCT_IMAGE).attr("src");

            final Element itemPriceElement = document.select(SELECTOR_PRODUCT_PRICE).first();
            final Integer price = Objects.isNull(itemPriceElement) ? 0 : Integer.parseInt(itemPriceElement.text().replace(",", ""));
            final String itemSaleStatusText = document.select(SELECTOR_PRODUCT_SALE_STATUS).text().trim();

            final ProductSaleStatus saleStatus = getProductSaleStatus(price, itemSaleStatusText);

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
                    .saleStatus(saleStatus)
                    .crawledAt(crawledAt)
                    .build();

        } catch (RuntimeException e) {
            return null;
        }
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
                return DISCONTINUE;
            case TEXT_PRODUCT_STATUS_EMPTY_ACCOUNT:
                return EMPTY_AMOUNT;
            case TEXT_PRODUCT_STATUS_NO_SUPPORT:
                return NOT_SUPPORT;
        }

        return ProductSaleStatus.UNKNOWN;
    }


}
