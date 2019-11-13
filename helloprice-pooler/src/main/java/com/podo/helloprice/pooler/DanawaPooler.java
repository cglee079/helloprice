package com.podo.helloprice.pooler;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import com.podo.helloprice.core.domain.item.ItemSaleStatus;
import com.podo.helloprice.core.util.MyHttpUtils;
import com.podo.helloprice.pooler.exception.FailGetDocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaPooler implements Pooler {

    public static final String DANAWA_URL = "http://www.danawa.com";
    public static final String DANAWA_REDIRECT_URL = "https://danawa.page.link";
    public static final String DANAWA_ITEM_URL = "http://prod.danawa.com/info/?pcode=";
    public static final String[] ITEM_CODE_PARAMKEYS = {"pcode", "code"};

    private static final String ITEM_NAME_SELECTOR = "#blog_content > div.summary_info > div.top_summary > h3";
    //    private static final String ITEM_NAME_SELECTOR = "head > meta[property=og:title]";
    private static final String ITEM_PRICE_SELECTOR = "span.lwst_prc > a > em";
    private static final String ITEM_IMAGE_SELECTOR = "#baseImage";
    private static final String ITEM_SALE_STATUS_SELECTOR = "div.lowest_area > div.no_data > p > strong";

    private static final String ITEM_STATUS_DISCONTINUE = "단종 된";
    private static final String ITEM_STATUS_EMPTY_ACCOUNT = "일시 품절";
    private static final String ITEM_STATUS_NO_SUPPORT = "가격비교 중지";


    private final JsoupDocumentLoader jsoupDocumentLoader;

    @Override
    public ItemInfoVo poolItem(String itemCode) {
        log.info("DANAWA 상품페이지 크롤을 시작합니다, 상품코드 : {}", itemCode);

        final String itemUrl = DANAWA_ITEM_URL + itemCode;

        log.info("CRAWL URL : {}", itemUrl);

        Document document;

        try {
            document = jsoupDocumentLoader.getDocument(itemUrl);
        } catch (FailGetDocumentException e) {
            log.error("아이템 페이지를 가져올 수 없습니다. {}", e.getMessage());
            return null;
        }


        try {
            final String itemName = document.select(ITEM_NAME_SELECTOR).text().replace("[다나와]", "").trim();
            final String itemImage = document.select(ITEM_IMAGE_SELECTOR).attr("src");

            final Element itemPriceElement = document.select(ITEM_PRICE_SELECTOR).first();
            final Integer itemPrice = Objects.isNull(itemPriceElement) ? 0 : Integer.valueOf(itemPriceElement.text().replace(",", ""));
            final String itemSaleStatusStr = document.select(ITEM_SALE_STATUS_SELECTOR).text();

            ItemSaleStatus itemSaleStatus;

            if (itemPrice != 0) {
                itemSaleStatus = ItemSaleStatus.SALE;
            } else {
                switch (itemSaleStatusStr.trim()) {
                    case ITEM_STATUS_DISCONTINUE:
                        itemSaleStatus = ItemSaleStatus.DISCONTINUE;
                        break;
                    case ITEM_STATUS_EMPTY_ACCOUNT:
                        itemSaleStatus = ItemSaleStatus.EMPTY_AMOUNT;
                        break;
                    case ITEM_STATUS_NO_SUPPORT:
                        itemSaleStatus = ItemSaleStatus.NOT_SUPPORT;
                        break;
                    default:
                        itemSaleStatus = ItemSaleStatus.UNKNOWN;
                        break;
                }
            }

            ItemInfoVo itemInfoVo = new ItemInfoVo(itemCode, itemName, itemImage, itemPrice, itemSaleStatus);

            log.info("상품 정보확인, '{}'", itemInfoVo);

            if (StringUtils.isEmpty(itemName)) {
                log.info("확인 할 수 없는 상품입니다, 상품 코드 : {}", itemCode);
                return null;
            }

            return itemInfoVo;

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return null;
        }

    }

    public String getItemCodeFromUrl(String url) {

        if (url.contains(DANAWA_REDIRECT_URL)) {

            Document document;

            try {
                document = jsoupDocumentLoader.getDocument(url);
            } catch (FailGetDocumentException e) {
                log.error("아이템 페이지를 가져올 수 없습니다. {}", e.getMessage());
                return null;
            }

            Element canonical = document.select("link[rel=canonical]").first();
            url = canonical.attr("href");
        }

        String itemCode;

        for (String key : ITEM_CODE_PARAMKEYS) {
            itemCode = MyHttpUtils.getParamValue(url, key);
            if (Objects.nonNull(itemCode)) {
                return itemCode;
            }
        }

        return null;
    }
}
