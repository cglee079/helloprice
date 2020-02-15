package com.podo.helloprice.crawl.worker.job;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.crawl.core.target.danawa.DanawaCrawler;
import com.podo.helloprice.core.domain.item.CrawledItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@StepScope
public class CrawlJobProcessor implements ItemProcessor<Item, Item> {

    @Value("${item.max_deadcount}")
    private Integer maxDeadCount;

    private final DanawaCrawler danawaCrawler;

    @Override
    public Item process(Item item) {
        final String existedItemName = item.getItemName();
        final String existedItemCode = item.getItemCode();
        final LocalDateTime now = LocalDateTime.now();

        log.info("{}({}) 상품의 정보 갱신을 실행합니다", existedItemName, existedItemCode);

        final CrawledItemVo crawledItem = danawaCrawler.crawlItem(existedItemCode);

        if (Objects.isNull(crawledItem)) {
            log.info("{}({}) 상품의 정보 갱신 에러 발생", existedItemName, existedItemCode);

            item.increaseDeadCount();

            if (item.hasDeadCountMoreThan(maxDeadCount)) {
                log.info("{}({}) 상품의 에러카운트 초과, DEAD 상태 변경", existedItemName, existedItemCode);
                item.died(now);
            }

            return item;
        }

        item.updateByCrawledItem(crawledItem, now);

        log.info("{}({}), 가격 : `{}`, 상품판매상태 : `{}`, 상품상태 `{}`", existedItemName, existedItemCode, item.getItemPrice(), item.getItemSaleStatus().getValue(), item.getItemStatus());

        return item;
    }
}
