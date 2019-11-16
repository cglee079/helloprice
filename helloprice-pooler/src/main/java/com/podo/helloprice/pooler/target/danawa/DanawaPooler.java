package com.podo.helloprice.pooler.target.danawa;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import com.podo.helloprice.core.domain.item.ItemSaleStatus;
import com.podo.helloprice.core.domain.item.ItemSearchResultVo;
import com.podo.helloprice.core.util.MyCurrencyUtils;
import com.podo.helloprice.core.util.MyHttpUtils;
import com.podo.helloprice.core.util.MyStringUtils;
import com.podo.helloprice.pooler.Pooler;
import com.podo.helloprice.pooler.exception.FailGetDocumentException;
import com.podo.helloprice.pooler.loader.DelayDocumentLoader;
import com.podo.helloprice.pooler.loader.PromptDocumentLoader;
import com.podo.helloprice.pooler.target.danawa.DanawaPoolConfig.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaPooler implements Pooler {

    private final DanawaItemCache danawaItemCache;
    private final DelayDocumentLoader delayDocumentLoader;
    private final PromptDocumentLoader promptDocumentLoader;

    @Override
    public ItemInfoVo poolItem(String itemCode) {

        final ItemInfoVo existCache = danawaItemCache.get(itemCode);
        if (Objects.nonNull(existCache)) {
            return existCache;
        }


        log.info("DANAWA '상품' 페이지 크롤을 시작합니다, 상품코드 : {}", itemCode);

        final String itemUrl = ItemPage.DANAWA_ITEM_URL + itemCode;

        log.info("크롤, URL : {}", itemUrl);

        Document document;

        try {
            document = promptDocumentLoader.getDocument(itemUrl);
        } catch (FailGetDocumentException e) {
            log.error("상품 페이지를 가져올 수 없습니다. {}", e.getMessage());
            return null;
        }


        try {
            final ItemInfoVo itemInfoVo = getItemInfoVo(document, itemCode, itemUrl);

            log.info("상품 정보확인, '{}'", itemInfoVo);

            if (StringUtils.isEmpty(itemInfoVo.getItemName())) {
                log.info("확인 할 수 없는 상품입니다, 상품 코드 : {}", itemCode);
                return null;
            }

            danawaItemCache.put(itemCode, itemInfoVo);

            return itemInfoVo;

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return null;
        }

    }

    private ItemInfoVo getItemInfoVo(Document document, String itemCode, String itemUrl) {
        final String itemName = document.select(ItemPage.ITEM_NAME_SELECTOR).text().replace("[다나와]", "").trim();
        final String itemDesc = document.select(ItemPage.ITEM_DESC_SELECTOR).text().replace("[다나와]", "").trim();
        final String itemImage = document.select(ItemPage.ITEM_IMAGE_SELECTOR).attr("src");

        final Element itemPriceElement = document.select(ItemPage.ITEM_PRICE_SELECTOR).first();
        final Integer itemPrice = Objects.isNull(itemPriceElement) ? 0 : Integer.valueOf(itemPriceElement.text().replace(",", ""));
        final String itemSaleStatusStr = document.select(ItemPage.ITEM_SALE_STATUS_SELECTOR).text();

        ItemSaleStatus itemSaleStatus;

        if (itemPrice != 0) {
            itemSaleStatus = ItemSaleStatus.SALE;
        } else {
            switch (itemSaleStatusStr.trim()) {
                case ItemPage.ITEM_STATUS_DISCONTINUE:
                    itemSaleStatus = ItemSaleStatus.DISCONTINUE;
                    break;
                case ItemPage.ITEM_STATUS_EMPTY_ACCOUNT:
                    itemSaleStatus = ItemSaleStatus.EMPTY_AMOUNT;
                    break;
                case ItemPage.ITEM_STATUS_NO_SUPPORT:
                    itemSaleStatus = ItemSaleStatus.NOT_SUPPORT;
                    break;
                default:
                    itemSaleStatus = ItemSaleStatus.UNKNOWN;
                    break;
            }
        }

        return new ItemInfoVo(itemCode, itemUrl, itemName, itemDesc, itemImage, itemPrice, itemSaleStatus);
    }

    public String getItemCodeFromUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        if (url.contains(GetCode.DANAWA_REDIRECT_URL)) {
            url = getRealUrlFromRedirect(url);
        }

        for (String key : GetCode.ITEM_CODE_PARAM_KEYS) {
            final String itemCode = MyHttpUtils.getParamValue(url, key);
            if (Objects.nonNull(itemCode)) {
                return itemCode;
            }
        }

        return null;
    }

    private String getRealUrlFromRedirect(String url) {
        Document document;

        try {
            document = promptDocumentLoader.getDocument(url);
        } catch (FailGetDocumentException e) {
            log.error("상품 페이지를 가져올 수 없습니다. {}", e.getMessage(), e);
            return "";
        }

        final Element canonical = document.select("link[rel=canonical]").first();

        return canonical.attr("href");
    }

    public List<ItemSearchResultVo> poolItemSearchResults(String keyword) {
        log.info("DANAWA 상품 '검색' 페이지 크롤을 시작합니다, 검색어 : {}", keyword);

        final List<ItemSearchResultVo> itemSearchInfos = new ArrayList<>();
        final String crawlUrl = SearchPage.DANAWA_ITEM_SEARCH_URL + keyword;

        log.info("CRAWL URL : {}", crawlUrl);

        Document document;

        try {
            document = delayDocumentLoader.getDocument(crawlUrl, Collections.singletonList(SearchPage.SEARCH_ITEM_LIST_SELECTOR));
        } catch (FailGetDocumentException e) {
            log.error("상품 검색 페이지를 가져올 수 없습니다. {}", e.getMessage(), e);
            return Collections.emptyList();
        }

        final Elements items = document.select(SearchPage.SEARCH_ITEM_LIST_SELECTOR);

        for (Element item : items) {
            final String itemUrl = item.select(SearchPage.SEARCH_ITEM_URL_SELECTOR).attr("href");
            final String itemName = item.select(SearchPage.SEARCH_ITEM_NAME_SELECTOR).text();
            final String itemPriceDesc = getSearchItemPriceDesc(item.select(SearchPage.SEARCH_ITEM_PRICE_SELECTOR).text());

            final String itemCode = getItemCodeFromUrl(itemUrl);
            final String itemDesc = itemName + " [" + itemPriceDesc + "]";
            final ItemSearchResultVo itemSearchResultVo = new ItemSearchResultVo(itemCode, itemDesc);

            if (itemSearchResultVo.validate() && !itemPriceDesc.equals(SearchPage.SEARCH_ITEM_STATUS_VALUE_DISCONTINUE)) {
                itemSearchInfos.add(itemSearchResultVo);
            }
        }

        return itemSearchInfos;
    }

    private String getSearchItemPriceDesc(String itemPriceStr) {
        final String itemPrice = itemPriceStr.replaceAll(",", "");

        if (MyStringUtils.isStringInteger(itemPrice)) {
            return MyCurrencyUtils.toExchangeRateKRWStr(Integer.valueOf(itemPrice));
        }

        return itemPriceStr;
    }

}
