package com.podo.itemwatcher.core.domain.useritem;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.QItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserItemRelationRepositoryCustomImpl extends QuerydslRepositorySupport implements UserItemRelationRepositoryCustom {

    private final QUserItemRelation userItemRelation;
    private final JPAQueryFactory queryFactory;

    public UserItemRelationRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(UserItemRelation.class);
        this.userItemRelation = QUserItemRelation.userItemRelation;
        this.queryFactory = queryFactory;
    }

}
