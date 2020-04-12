package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.telegram.domain.user.model.UserStatus;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.podo.helloprice.telegram.domain.product.QProduct.product;
import static com.podo.helloprice.telegram.domain.user.model.QUser.user;
import static com.podo.helloprice.telegram.domain.userproduct.QUserProductNotify.userProductNotify;

@RequiredArgsConstructor
public class UserProductNotifyRepositoryCustomImpl implements UserProductNotifyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserProductNotify> findByUserTelegramId(String telegramId) {
        return jpaQueryFactory.selectFrom(userProductNotify)
                .leftJoin(userProductNotify.user, user)
                .leftJoin(userProductNotify.product, product)
                .where(  userProductNotify.user.telegramId.eq(telegramId))
                .orderBy(userProductNotify.createAt.desc())
                .fetch();
    }

    @Override
    public List<UserProductNotify> findByProductIdAndUserStatus(Long productId, UserStatus userStatus) {
        return jpaQueryFactory.selectFrom(userProductNotify)
                .leftJoin(userProductNotify.user, user)
                .leftJoin(userProductNotify.product, product)
                .where(userProductNotify.product.id.eq(productId))
                .where(userProductNotify.user.userStatus.eq(userStatus))
                .fetch();
    }
}
