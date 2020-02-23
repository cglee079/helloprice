package com.podo.helloprice.core.domain.item.repository;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.model.ItemStatus;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;

import static com.podo.helloprice.core.domain.item.QItem.item;

public class ItemRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemRepositoryCustom {


    public ItemRepositoryCustomImpl() {
        super(Item.class);
    }

    @Override
    public Item findOneByLastCrawledBeforePublishAt(ItemStatus itemStatus, LocalDateTime expirePoolAt) {
        return from(item)
                .where(item.itemStatus.eq(itemStatus))
                .where(item.lastPublishAt.lt(expirePoolAt))
                .orderBy(item.lastPublishAt.asc())
                .limit(1)
                .fetchOne();
    }
}
