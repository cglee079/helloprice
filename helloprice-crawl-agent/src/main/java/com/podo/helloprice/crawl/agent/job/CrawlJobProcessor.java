package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.core.domain.item.CrawledItem;
import com.podo.helloprice.crawl.core.vo.LastCrawledItem;
import com.podo.helloprice.crawl.worker.target.danawa.DanawaCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@StepScope
public class CrawlJobProcessor implements ItemProcessor<LastCrawledItem, CrawledItem> {
    private final DanawaCrawler danawaCrawler;

    @Override
    public CrawledItem process(LastCrawledItem lastCrawledItem) {
        final String existedItemName = lastCrawledItem.getItemName();
        final String existedItemCode = lastCrawledItem.getItemCode();

        log.info("{}({}) 상품의 정보 갱신을 실행합니다", existedItemName, existedItemCode);

        final CrawledItem crawledItem = danawaCrawler.crawlItem(existedItemCode);

        return crawledItem;
    }
}
