package com.podo.helloprice.crawl.worker.target.danawa.parser;

import com.podo.helloprice.core.util.CurrencyUtil;
import com.podo.helloprice.core.util.NumberUtil;
import com.podo.helloprice.core.util.StringUtil;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductCodeCrawler;
import com.podo.helloprice.crawl.worker.vo.ProductSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductSearchVoParser {

    private static String SELECTOR_SEARCH_PRODUCT_NAME = "> div.goods_info_wrap > div > div > a > p";
    private static String SELECTOR_SEARCH_PRODUCT_PRICE = "> div.goods_info_wrap > div > div > a > div.price_tarea > span > em";
    private static String SELECTOR_SEARCH_PRODUCT_URL = "> div.goods_info_wrap > a";
    private static String TEXT_SEARCH_PRODUCT_STATUS_VALUE_DISCONTINUE = "단종";
    private static String TEXT_SEARCH_PRODUCT_STATUS_VALUE_EMPTY_ACCOUNT = "일시품절";
    
    public List<ProductSearchVo> parse(DanawaProductCodeCrawler danawaProductCodeCrawler, Elements products) {
        final List<ProductSearchVo> productSearchInfos = new ArrayList<>();

        for (Element product : products) {
            final String productUrl = product.select(SELECTOR_SEARCH_PRODUCT_URL).attr("href");
            final String productName = product.select(SELECTOR_SEARCH_PRODUCT_NAME).text();
            final String productPriceDesc = getSearchProductPriceDesc(product.select(SELECTOR_SEARCH_PRODUCT_PRICE).text());
            final String productCode = danawaProductCodeCrawler.crawl(productUrl);
            final String productDesc = productName + " [" + productPriceDesc + "]";

            if (validateProductInfo(productCode, productDesc) && !productPriceDesc.equals(TEXT_SEARCH_PRODUCT_STATUS_VALUE_DISCONTINUE)) {
                productSearchInfos.add(new ProductSearchVo(productCode, productDesc));
            }
        }

        return productSearchInfos;
    }

    private String getSearchProductPriceDesc(String productPriceText) {
        final String productPrice = productPriceText.replaceAll(",", "");

        if (NumberUtil.isInteger(productPrice)) {
            return CurrencyUtil.toKRW(Integer.parseInt(productPrice));
        }

        return productPriceText;
    }

    private boolean validateProductInfo(String productCode, String description) {
        if (StringUtil.isEmpty(productCode)) {
            return false;
        }

        if (!NumberUtil.isInteger(productCode)) {
            return false;
        }

        return !StringUtil.isEmpty(description);
    }


}
