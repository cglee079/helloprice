package com.podo.helloprice.crawl.worker;


import com.podo.helloprice.core.domain.item.vo.CrawledItem;

import java.time.LocalDateTime;

public interface Crawler {

    CrawledItem crawlItem(String itemCode, LocalDateTime crawledAt);
}
