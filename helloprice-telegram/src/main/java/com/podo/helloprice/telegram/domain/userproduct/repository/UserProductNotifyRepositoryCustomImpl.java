package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.telegram.domain.product.model.QProduct;
import com.podo.helloprice.telegram.domain.user.model.QUser;
import com.podo.helloprice.telegram.domain.userproduct.QUserProductNotify;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.podo.helloprice.telegram.domain.userproduct.QUserProductNotify.userProductNotify;


@RequiredArgsConstructor
public class UserProductNotifyRepositoryCustomImpl implements UserProductNotifyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserProductNotify> findByTelegramId(String telegramId) {
        return jpaQueryFactory.selectFrom(userProductNotify)
                .leftJoin(userProductNotify.user, QUser.user)
                .leftJoin(userProductNotify.product, QProduct.product)
                .where(userProductNotify.user.telegramId.eq(telegramId))
                .orderBy(userProductNotify.createAt.desc())
                .fetch();
    }

}
