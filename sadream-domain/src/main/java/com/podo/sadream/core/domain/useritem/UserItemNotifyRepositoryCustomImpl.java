package com.podo.sadream.core.domain.useritem;

import com.podo.sadream.core.domain.item.QItem;
import com.podo.sadream.core.domain.user.QUser;
import com.podo.sadream.core.domain.user.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserItemNotifyRepositoryCustomImpl extends QuerydslRepositorySupport implements UserItemNotifyRepositoryCustom {

    private final QUserItemNotify userItemNotify;
    private final JPAQueryFactory queryFactory;

    public UserItemNotifyRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(UserItemNotify.class);
        this.userItemNotify = QUserItemNotify.userItemNotify;
        this.queryFactory = queryFactory;
    }

    @Override
    public List<UserItemNotify> findByItemIdAndUserStatus(Long itemId, UserStatus userStatus) {
        return from(userItemNotify)
                .leftJoin(userItemNotify.user, QUser.user)
                .leftJoin(userItemNotify.item, QItem.item)
                .where(userItemNotify.item.id.eq(itemId))
                .where(userItemNotify.user.userStatus.eq(userStatus))
                .fetch();
    }

    @Override
    public List<UserItemNotify> findByUserTelegramId(String telegramId) {
        return from(userItemNotify)
                .leftJoin(userItemNotify.user, QUser.user)
                .leftJoin(userItemNotify.item, QItem.item)
                .where(userItemNotify.user.telegramId.eq(telegramId))
                .fetch();
    }
}
