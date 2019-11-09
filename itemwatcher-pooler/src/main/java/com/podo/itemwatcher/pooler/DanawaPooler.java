package com.podo.itemwatcher.pooler;

import com.podo.itemwatcher.core.domain.item.ItemInfoVo;
import com.podo.itemwatcher.core.domain.item.ItemSaleStatus;
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

    private static final String DANAWA_ITEM_URL = "http://prod.danawa.com/info/?pcode=";

    private static final String ITEM_NAME_SELECTOR = "#blog_content > div.summary_info > div.top_summary > h3";
    private static final String ITEM_PRICE_SELECTOR = "span.lwst_prc > a > em";
    private static final String ITEM_IMAGE_SELECTOR = "#baseImage";
    private static final String ITEM_SALE_STATUS_SELECTOR = "div.lowest_area > div.no_data > p > strong";

    private static final String ITEM_STATUS_DISCONTINUE = "단종 된";
    private static final String ITEM_STATUS_EMPTY_ACCOUNT = "일시 품절";
    private static final String ITEM_STATUS_NO_SUPPORT = "가격비교 중지";


    private final JsoupDocumentLoader jsoupDocumentLoader;

    @Override
    public ItemInfoVo poolItem(String itemCode) {

        final String itemUrl = DANAWA_ITEM_URL + itemCode;
        Document document = jsoupDocumentLoader.getDocument(itemUrl);

        try {
            final String itemName = document.select(ITEM_NAME_SELECTOR).text();
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
                        itemSaleStatus = ItemSaleStatus.EMPTY_ACCOUNT;
                        break;
                    case ITEM_STATUS_NO_SUPPORT:
                        itemSaleStatus = ItemSaleStatus.NO_SUPPORT;
                        break;
                    default:
                        itemSaleStatus = ItemSaleStatus.ERROR;
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
}
