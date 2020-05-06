package com.podo.helloprice.api.domain.product.repository;


import com.podo.helloprice.api.domain.product.model.Product;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.podo.helloprice.api.domain.product.model.QProductPrice.productPrice;
import static com.podo.helloprice.core.enums.SaleType.NORMAL;
import static com.podo.helloprice.core.enums.ProductSaleStatus.SALE;

@RequiredArgsConstructor
@Repository
public class ProductQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public Map<Product, Double> findTopDecrease(LocalDateTime now) {
        final NumberExpression<Double> changeRate =
                productPrice.price.doubleValue()
                        .subtract(productPrice.prevPrice)
                        .divide(productPrice.prevPrice);

        return queryFactory
                .select(productPrice.product, changeRate)
                .from(productPrice)
                .where(productPrice.product.saleStatus.eq(SALE))
                .where(productPrice.lastUpdateAt.lt(now))
                .where(productPrice.lastUpdateAt.goe(now.truncatedTo(ChronoUnit.DAYS)))
                .where(productPrice.priceType.eq(NORMAL))
                .orderBy(changeRate.asc())
                .transform(GroupBy.groupBy(productPrice.product).as(changeRate));
    }

    public void findByUserId(String userId) {
        queryFactory.

    }
}
