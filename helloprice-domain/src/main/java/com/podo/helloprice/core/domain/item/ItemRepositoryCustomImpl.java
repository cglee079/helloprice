package com.podo.helloprice.core.domain.item;

import com.podo.helloprice.core.domain.useritem.QUserItemNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ItemRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemRepositoryCustom {

    private final QItem item;
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Item.class);
        this.item = QItem.item;
        this.queryFactory = queryFactory;
    }
}
