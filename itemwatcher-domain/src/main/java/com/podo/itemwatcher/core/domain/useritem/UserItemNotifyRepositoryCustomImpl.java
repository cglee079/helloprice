package com.podo.itemwatcher.core.domain.useritem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserItemNotifyRepositoryCustomImpl extends QuerydslRepositorySupport implements UserItemNotifyRepositoryCustom {

    private final QUserItemNotify userItemNotify;
    private final JPAQueryFactory queryFactory;

    public UserItemNotifyRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(UserItemNotify.class);
        this.userItemNotify = QUserItemNotify.userItemNotify;
        this.queryFactory = queryFactory;
    }

}
