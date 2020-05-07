package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.telegram.domain.productsale.QProductSale;
import com.podo.helloprice.telegram.domain.user.model.QUser;
import com.podo.helloprice.telegram.domain.userproduct.UserProductSaleNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.podo.helloprice.telegram.domain.userproduct.QUserProductSaleNotify.userProductSaleNotify;


@RequiredArgsConstructor
public class UserProductNotifyRepositoryCustomImpl implements UserProductNotifyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserProductSaleNotify> findByTelegramId(String telegramId) {
        return queryFactory.selectFrom(userProductSaleNotify)
                .leftJoin(userProductSaleNotify.user, QUser.user)
                .leftJoin(userProductSaleNotify.productSale, QProductSale.productSale)
                .where(userProductSaleNotify.user.telegramId.eq(telegramId))
                .orderBy(userProductSaleNotify.createAt.desc())
                .fetch();
    }

}
