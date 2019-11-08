package com.podo.itemwatcher.scheduler.job;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.pooler.DanawaPooler;
import com.podo.itemwatcher.pooler.domain.ItemInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@StepScope
public class PoolerJobProcessor implements ItemProcessor<Item, Item> {

    private static final Integer MAX_DEADCOUNT = 2;

    private final DanawaPooler danawaPooler;

    @Override
    public Item process(Item item) {

        final String itemCode = item.getItemCode();

        final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);

        if (Objects.isNull(itemInfoVo)) {
            item.increaseDeadCount();

            if (item.getDeadCount() > MAX_DEADCOUNT) {

                item.died();
            }

            return item;
        }

        item.updateInfo(itemInfoVo.getItemName(), itemInfoVo.getItemImage(), itemInfoVo.getItemPrice(), LocalDateTime.now());

        return item;
    }
}
