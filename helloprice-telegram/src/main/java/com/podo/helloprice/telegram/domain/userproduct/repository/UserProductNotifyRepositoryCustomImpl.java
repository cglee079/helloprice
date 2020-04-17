package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.core.model.UserStatus;
import com.podo.helloprice.telegram.domain.product.model.QProduct;
import com.podo.helloprice.telegram.domain.user.model.QUser;
import com.podo.helloprice.telegram.domain.userproduct.QUserProductNotify;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserProductNotifyRepositoryCustomImpl implements UserProductNotifyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserProductNotify> findByUserTelegramId(String telegramId) {
        return jpaQueryFactory.selectFrom(QUserProductNotify.userProductNotify)
                .leftJoin(QUserProductNotify.userProductNotify.user, QUser.user)
                .leftJoin(QUserProductNotify.userProductNotify.product, QProduct.product)
                .where(  QUserProductNotify.userProductNotify.user.telegramId.eq(telegramId))
                .orderBy(QUserProductNotify.userProductNotify.createAt.desc())
                .fetch();
    }

    @Override
    public List<UserProductNotify> findByProductIdAndUserStatus(Long productId, UserStatus userStatus) {
        return jpaQueryFactory.selectFrom(QUserProductNotify.userProductNotify)
                .leftJoin(QUserProductNotify.userProductNotify.user, QUser.user)
                .leftJoin(QUserProductNotify.userProductNotify.product, QProduct.product)
                .where(QUserProductNotify.userProductNotify.product.id.eq(productId))
                .where(QUserProductNotify.userProductNotify.user.userStatus.eq(userStatus))
                .fetch();
    }
}
