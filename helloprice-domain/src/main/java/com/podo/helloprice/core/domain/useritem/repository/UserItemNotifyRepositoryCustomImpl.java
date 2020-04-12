package com.podo.helloprice.core.domain.useritem.repository;

import com.podo.helloprice.core.domain.item.QProduct;
import com.podo.helloprice.core.domain.user.QUser;
import com.podo.helloprice.core.domain.user.model.UserStatus;
import com.podo.helloprice.core.domain.useritem.UserProductNotify;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.podo.helloprice.core.domain.useritem.QUserProductNotify.userProductNotify;

public class UserProductNotifyRepositoryCustomImpl extends QuerydslRepositorySupport implements UserProductNotifyRepositoryCustom {

    public UserProductNotifyRepositoryCustomImpl() {
        super(UserProductNotify.class);
    }

    @Override
    public List<UserProductNotify> findByProductIdAndUserStatus(Long itemId, UserStatus userStatus) {
        return from(userProductNotify)
                .leftJoin(userProductNotify.user, QUser.user)
                .leftJoin(userProductNotify.item, QProduct.item)
                .where(userProductNotify.item.id.eq(itemId))
                .where(userProductNotify.user.userStatus.eq(userStatus))
                .fetch();
    }

    @Override
    public List<UserProductNotify> findByUserTelegramId(String telegramId) {
        return from(userProductNotify)
                .leftJoin(userProductNotify.user, QUser.user)
                .leftJoin(userProductNotify.item, QProduct.item)
                .where(userProductNotify.user.telegramId.eq(telegramId))
                .orderBy(userProductNotify.createAt.desc())
                .fetch();
    }
}
