package com.podo.sadream.core.domain.user;

import com.podo.itemwatcher.core.domain.item.QItem;
import com.podo.itemwatcher.core.domain.useritem.QUserItemNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    private final QUser user;
    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.user = QUser.user;
        this.queryFactory = queryFactory;
    }

}
