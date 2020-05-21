package com.podo.helloprice.telegram.domain.tusernotify.repository;

import com.podo.helloprice.telegram.domain.productsale.QProductSale;
import com.podo.helloprice.telegram.domain.tuser.model.QTUser;
import com.podo.helloprice.telegram.domain.tusernotify.QTUserNotify;
import com.podo.helloprice.telegram.domain.tusernotify.TUserNotify;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.podo.helloprice.telegram.domain.tusernotify.QTUserNotify.tUserNotify;


@RequiredArgsConstructor
public class TUserNotifyRepositoryCustomImpl implements TUserNotifyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TUserNotify> findByTelegramId(String telegramId) {
        return queryFactory.selectFrom(tUserNotify)
                .leftJoin(tUserNotify.tUser, QTUser.tUser)
                .leftJoin(tUserNotify.productSale, QProductSale.productSale)
                .where(tUserNotify.tUser.telegramId.eq(telegramId))
                .orderBy(tUserNotify.createAt.desc())
                .fetch();
    }

}
