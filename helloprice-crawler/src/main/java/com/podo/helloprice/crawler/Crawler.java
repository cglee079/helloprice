package com.podo.helloprice.crawler;


import com.podo.helloprice.core.domain.item.CrawledItemVo;

public interface Crawler {

    CrawledItemVo crawlItem(String itemCode);
}
