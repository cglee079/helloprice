package com.podo.helloprice.core.domain.item;

import com.podo.helloprice.core.domain.useritem.QUserItemNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemRepositoryCustom {

    private final QItem item;
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Item.class);
        this.item = QItem.item;
        this.queryFactory = queryFactory;
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
