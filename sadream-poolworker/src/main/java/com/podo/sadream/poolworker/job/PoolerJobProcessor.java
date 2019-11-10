package com.podo.sadream.poolworker.job;

import com.podo.sadream.core.domain.item.Item;
import com.podo.sadream.pooler.DanawaPooler;
import com.podo.sadream.core.domain.item.ItemInfoVo;
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

    private static final Integer MAX_DEADCOUNT = 3;

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

        item.updateInfo(itemInfoVo, LocalDateTime.now());

        return item;
    }
}
