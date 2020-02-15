package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.core.domain.item.CrawledItem;
import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.ItemRepository;
import com.podo.helloprice.crawl.core.vo.LastCrawledItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CrawlJobWriter implements ItemWriter<CrawledItem> {


    @Value("${item.max_deadcount}")
    private Integer maxDeadCount;

    private final CrawlJobParameter crawlJobParameter;
    private final ItemRepository itemRepository;

    @Override
    public void write(List<? extends CrawledItem> crawledItems) {

        for (CrawledItem crawledItem : crawledItems) {
            updateItem(crawledItem);
        }
    }

    private void updateItem(CrawledItem crawledItem) {
        final LastCrawledItem lastCrawledItem = crawlJobParameter.getLastCrawledItem();
        final String existedItemName = lastCrawledItem.getItemName();
        final String existedItemCode = lastCrawledItem.getItemCode();
        final LocalDateTime now = LocalDateTime.now();

        final Item item = itemRepository.findByItemCode(existedItemCode);

        if (Objects.isNull(crawledItem)) {


            log.info("{}({}) 상품의 정보 갱신 에러 발생", existedItemName, existedItemCode);

            item.increaseDeadCount();

            if (item.hasDeadCountMoreThan(maxDeadCount)) {
                log.info("{}({}) 상품의 에러카운트 초과, DEAD 상태 변경", existedItemName, existedItemCode);
                item.died(now);
            }
        }

        item.updateByCrawledItem(crawledItem, now);

        log.info("{}({}), 가격 : `{}`, 상품판매상태 : `{}`, 상품상태 `{}`", existedItemName, existedItemCode, item.getItemPrice(), item.getItemSaleStatus().getValue(), item.getItemStatus());

    }


}
