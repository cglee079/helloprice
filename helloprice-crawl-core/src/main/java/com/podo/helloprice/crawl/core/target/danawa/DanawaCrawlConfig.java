package com.podo.helloprice.crawl.core.target.danawa;

public class DanawaCrawlConfig {

    public interface Global {
        String DANAWA_URL = "http://www.danawa.com";
    }

    public interface GetItemCode {
        String DANAWA_REDIRECT_URL = "https://danawa.page.link";
        String[] ITEM_CODE_PARAM_KEYS = {"pcode", "code"};
    }

    public interface ItemPage {
        String DANAWA_ITEM_URL = "http://prod.danawa.com/info/?pcode=";
        String ITEM_NAME_SELECTOR = "#blog_content > div.summary_info > div.top_summary > h3";
        String ITEM_DESC_SELECTOR = "#blog_content .spec_list";
        String ITEM_PRICE_SELECTOR = "span.lwst_prc > a > em";
        String ITEM_IMAGE_SELECTOR = "#baseImage";
        String ITEM_SALE_STATUS_SELECTOR = "div.lowest_area > div.no_data > p > strong";
        String ITEM_STATUS_DISCONTINUE = "단종 된";
        String ITEM_STATUS_EMPTY_ACCOUNT = "일시 품절";
        String ITEM_STATUS_NO_SUPPORT = "가격비교 중지";
    }

    public interface SearchPage {
        String DANAWA_ITEM_SEARCH_URL = "http://search.danawa.com/mobile/dsearch.php?keyword=";
        String SEARCH_ITEM_LIST_SELECTOR = "#productListArea_list > li";
        String SEARCH_ITEM_NAME_SELECTOR = "> div.goods_info_wrap > div > div > a > p";
        String SEARCH_ITEM_PRICE_SELECTOR = "> div.goods_info_wrap > div > div > a > div.price_tarea > span > em";
        String SEARCH_ITEM_URL_SELECTOR = "> div.goods_info_wrap > a";
        String SEARCH_ITEM_STATUS_VALUE_DISCONTINUE = "단종";
        String SEARCH_ITEM_STATUS_VALUE_EMPTY_ACCOUNT = "일시품절";
    }
}
