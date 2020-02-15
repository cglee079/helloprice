package com.podo.helloprice.crawl.core;


import com.podo.helloprice.core.domain.item.CrawledItemVo;

public interface Crawler {

    CrawledItemVo crawlItem(String itemCode);
}
