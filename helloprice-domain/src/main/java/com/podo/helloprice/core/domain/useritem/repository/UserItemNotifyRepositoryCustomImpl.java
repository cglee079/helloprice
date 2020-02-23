package com.podo.helloprice.core.domain.useritem.repository;

import com.podo.helloprice.core.domain.item.QItem;
import com.podo.helloprice.core.domain.user.QUser;
import com.podo.helloprice.core.domain.user.model.UserStatus;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.podo.helloprice.core.domain.useritem.QUserItemNotify.userItemNotify;

public class UserItemNotifyRepositoryCustomImpl extends QuerydslRepositorySupport implements UserItemNotifyRepositoryCustom {

    public UserItemNotifyRepositoryCustomImpl() {
        super(UserItemNotify.class);
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
                .orderBy(userItemNotify.createAt.desc())
                .fetch();
    }
}
