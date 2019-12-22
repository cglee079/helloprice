package com.podo.helloprice.crawler.target.danawa;

import com.podo.helloprice.core.domain.item.CrawledItemVo;
import com.podo.helloprice.core.domain.item.ItemSaleStatus;
import com.podo.helloprice.core.domain.item.ItemSearchResultVo;
import com.podo.helloprice.core.util.MyCurrencyUtils;
import com.podo.helloprice.core.util.MyHttpUtils;
import com.podo.helloprice.core.util.MyNumberUtils;
import com.podo.helloprice.crawler.Crawler;
import com.podo.helloprice.crawler.exception.FailGetDocumentException;
import com.podo.helloprice.crawler.loader.DelayDocumentLoader;
import com.podo.helloprice.crawler.loader.PromptDocumentLoader;
import com.podo.helloprice.crawler.target.danawa.DanawaCrawlConfig.*;
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
public class DanawaCrawler implements Crawler {

    private final DelayDocumentLoader delayDocumentLoader;
    private final PromptDocumentLoader promptDocumentLoader;

    @Override
    public CrawledItemVo crawlItem(String itemCode) {

        log.info("DANAWA '상품' 페이지 크롤을 시작합니다, 상품코드 : {}", itemCode);

        final String itemUrl = ItemPage.DANAWA_ITEM_URL + itemCode;

        log.info("크롤, URL : {}", itemUrl);

        final Document document = getDocumentByPromptDocumentLoader(itemUrl);
        if (Objects.isNull(document)) {
            log.error("상품 상세 페이지를 가져올 수 없습니다");
            return null;
        }

        final CrawledItemVo crawledItem = getCrawledItemVoFromDocument(document, itemCode, itemUrl);
        if (Objects.isNull(crawledItem)) {
            log.info("확인 할 수 없는 상품입니다, 상품 코드 : {}", itemCode);
            return null;
        }

        log.info("상품 정보확인, '{}'", crawledItem);

        return crawledItem;

    }

    private Document getDocumentByPromptDocumentLoader(String itemUrl) {
        try {
            return promptDocumentLoader.getDocument(itemUrl);
        } catch (FailGetDocumentException e) {
            log.error("", e);
            return null;
        }
    }

    private CrawledItemVo getCrawledItemVoFromDocument(Document document, String itemCode, String itemUrl) {
        try {
            final String itemName = document.select(ItemPage.ITEM_NAME_SELECTOR).text().replace("[다나와]", "").trim();
            final String itemDesc = document.select(ItemPage.ITEM_DESC_SELECTOR).text().replace("[다나와]", "").trim();
            final String itemImage = document.select(ItemPage.ITEM_IMAGE_SELECTOR).attr("src");

            final Element itemPriceElement = document.select(ItemPage.ITEM_PRICE_SELECTOR).first();
            final Integer itemPrice = Objects.isNull(itemPriceElement) ? 0 : Integer.valueOf(itemPriceElement.text().replace(",", ""));
            final String itemSaleStatusText = document.select(ItemPage.ITEM_SALE_STATUS_SELECTOR).text().trim();

            ItemSaleStatus itemSaleStatus;

            itemSaleStatus = getItemSaleStatus(itemPrice, itemSaleStatusText);

            if (StringUtils.isEmpty(itemName)) {
                return null;
            }

            return CrawledItemVo.builder()
                    .itemCode(itemCode)
                    .itemUrl(itemUrl)
                    .itemName(itemName)
                    .itemDesc(itemDesc)
                    .itemImage(itemImage)
                    .itemPrice(itemPrice)
                    .itemSaleStatus(itemSaleStatus)
                    .build();

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private ItemSaleStatus getItemSaleStatus(Integer itemPrice, String itemSaleStatusText) {
        if (itemPrice != 0) {
            return ItemSaleStatus.SALE;
        }

        return getItemSaleStatusByCrawledSaleStatusText(itemSaleStatusText);
    }

    private ItemSaleStatus getItemSaleStatusByCrawledSaleStatusText(String itemSaleStatusStr) {
        ItemSaleStatus itemSaleStatus;
        switch (itemSaleStatusStr) {
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
        return itemSaleStatus;
    }

    public String getItemCodeFromUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        if (url.contains(GetItemCode.DANAWA_REDIRECT_URL)) {
            url = getRealUrlFromRedirectPage(url);
        }

        for (String key : GetItemCode.ITEM_CODE_PARAM_KEYS) {
            final String itemCode = MyHttpUtils.getParamValueFromUrl(url, key);
            if (Objects.nonNull(itemCode)) {
                return itemCode;
            }
        }

        return null;
    }

    private String getRealUrlFromRedirectPage(String url) {
        final Document document = getDocumentByPromptDocumentLoader(url);

        if (Objects.isNull(document)) {
            log.error("리다이렉트 페이지를 가져올 수 없습니다");
            return "";
        }

        final Element canonical = document.select("link[rel=canonical]").first();

        return canonical.attr("href");
    }

    public List<ItemSearchResultVo> crawlItemSearchResults(String keyword) {
        log.info("DANAWA 상품 '검색' 페이지 크롤을 시작합니다, 검색어 : {}", keyword);

        final String crawlUrl = SearchPage.DANAWA_ITEM_SEARCH_URL + keyword;

        log.info("CRAWL URL : {}", crawlUrl);

        final Document document = getDocumentByDelayDocumentLoader(crawlUrl);
        if (Objects.isNull(document)) {
            log.error("상품 검색 페이지를 가져올 수 없습니다");
            return Collections.emptyList();
        }


        final Elements itemSearchResultElements = document.select(SearchPage.SEARCH_ITEM_LIST_SELECTOR);
        final List<ItemSearchResultVo> itemSearchResults = getItemSearchResultsFromElements(itemSearchResultElements);

        return itemSearchResults;
    }

    private Document getDocumentByDelayDocumentLoader(String crawlUrl) {
        try {
            return delayDocumentLoader.getDocument(crawlUrl, Collections.singletonList(SearchPage.SEARCH_ITEM_LIST_SELECTOR));
        } catch (FailGetDocumentException e) {
            log.error("", e);
            return null;
        }
    }

    private List<ItemSearchResultVo> getItemSearchResultsFromElements(Elements items) {
        final List<ItemSearchResultVo> itemSearchInfos = new ArrayList<>();
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

        if (MyNumberUtils.isInteger(itemPrice)) {
            return MyCurrencyUtils.toKrw(Integer.valueOf(itemPrice));
        }

        return itemPriceStr;
    }

}
