package com.podo.itemwatcher.pooler.job.pooler;

import com.podo.itemwatcher.core.domain.item.ItemInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DanawaPooler implements Pooler {

    private static final String DANAWA_ITEM_URL = "http://prod.danawa.com/info/?pcode=";
    private static final String ITEM_NAME_SELECTOR = "#blog_content > div.summary_info > div.top_summary > h3";
    private static final String ITEM_PRICE_SELECTOR = "span.lwst_prc > a > em";
    private static final String ITEM_IMAGE_SELECTOR = "#baseImage";

    private final JsoupDocumentLoader jsoupDocumentLoader;

    @Override
    public ItemInfoVo poolItem(String itemCode) {

        Document document = jsoupDocumentLoader.getDocument(DANAWA_ITEM_URL + itemCode);

        try {
            final String itemName = document.select(ITEM_NAME_SELECTOR).text();
            final String itemImage = document.select(ITEM_IMAGE_SELECTOR).attr("src");
            final Integer itemPrice = Integer.valueOf(document.select(ITEM_PRICE_SELECTOR).text().replace(",", ""));

            ItemInfoVo itemInfoVo = new ItemInfoVo(itemName, itemImage, itemPrice);

            log.info("Pool ItemInfo, Code {}, ItemInfo : '{}'", itemCode, itemInfoVo);

            return itemInfoVo;

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return null;
        }


    }
}
