package com.podo.itemwatcher.core.domain.item;

import com.podo.itemwatcher.core.domain.user.QUser;
import com.podo.itemwatcher.core.domain.user.User;
import com.podo.itemwatcher.core.domain.user.UserRepositoryCustom;
import com.podo.itemwatcher.core.domain.useritem.QUserItemRelation;
import com.podo.itemwatcher.core.domain.useritem.UserItemRelation;
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

    @Override
    public List<Item> findByUserTelegramId(String telegramId) {

        return queryFactory.select(QUserItemRelation.userItemRelation.item)
                .from(QUserItemRelation.userItemRelation)
                .where(QUserItemRelation.userItemRelation.user.telegramId.eq(telegramId))
                .fetch();

    }
}
