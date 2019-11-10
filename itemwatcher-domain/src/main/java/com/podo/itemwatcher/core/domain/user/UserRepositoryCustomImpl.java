package com.podo.itemwatcher.core.domain.user;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.QItem;
import com.podo.itemwatcher.core.domain.useritem.QUserItemNotify;
import com.podo.itemwatcher.core.domain.useritem.UserItemNotify;
import com.podo.itemwatcher.core.domain.useritem.UserItemNotifyRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    private final QUser user;
    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.user = QUser.user;
        this.queryFactory = queryFactory;
    }

}
