package com.podo.helloprice.crawl.worker;


import com.podo.helloprice.core.domain.item.CrawledItem;

public interface Crawler {

    CrawledItem crawlItem(String itemCode);
}
