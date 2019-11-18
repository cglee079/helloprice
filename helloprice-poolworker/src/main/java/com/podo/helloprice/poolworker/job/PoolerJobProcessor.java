package com.podo.helloprice.poolworker.job;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.pooler.target.danawa.DanawaPooler;
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
public class PoolerJobProcessor implements ItemProcessor<Item, Item> {

    @Value("${item.max_deadcount}")
    private Integer maxDeadCount;

    private final DanawaPooler danawaPooler;

    @Override
    public Item process(Item item) {
        log.info("{}({}) 상품의 정보 갱신을 실행합니다", item.getItemName(), item.getItemCode());

        final String itemCode = item.getItemCode();

        final LocalDateTime poolAt = LocalDateTime.now();
        final CrawledItemVo crawledItem = danawaPooler.poolItem(itemCode);

        if (Objects.isNull(crawledItem)) {
            log.info("{}({}) 상품의 정보 갱신 에러 발생", item.getItemName(), item.getItemCode());

            item.increaseDeadCount();

            if (item.getDeadCount() > maxDeadCount) {
                log.info("{}({}) 상품의 에러카운트 초과, DEAD 상태 변경", item.getItemName(), item.getItemCode());

                item.died(poolAt);
            }

            return item;
        }

        item.updateByCrawledItem(crawledItem, poolAt);

        log.info("{}({}), 가격 : `{}`, 상품판매상태 : `{}`, 상품상태 `{}`", item.getItemName(), item.getItemCode(), item.getItemPrice(), item.getItemSaleStatus().getValue(), item.getItemStatus());
        return item;
    }
}
