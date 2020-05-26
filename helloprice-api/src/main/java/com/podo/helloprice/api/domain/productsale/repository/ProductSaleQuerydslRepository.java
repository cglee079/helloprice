package com.podo.helloprice.api.domain.productsale.repository;


import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.product.model.QProduct;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jdk.nashorn.internal.codegen.types.BitwiseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.podo.helloprice.api.domain.productsale.QProductSale.productSale;


@RequiredArgsConstructor
@Repository
public class ProductSaleQuerydslRepository {

    private final JPAQueryFactory queryFactory;


    public List<ProductSale> findTopDecrease(int limit, LocalDateTime now) {
        final NumberExpression<BigDecimal> priceChangeRate = productSale.price.castToNum(BigDecimal.class)
                .subtract(productSale.prevPrice.castToNum(BigDecimal.class))
                .divide(productSale.prevPrice.castToNum(BigDecimal.class));

        return queryFactory.selectFrom(productSale)
                .limit(limit)
                .orderBy(priceChangeRate.asc())
                .fetch();
    }

    public Map<Product, List<ProductSale>> findInProducts(List<Product> products) {
        return queryFactory.selectFrom(productSale)
                .where(productSale.product.in(products))
                .transform(
                        GroupBy.groupBy(productSale.product)
                                .as(GroupBy.list(productSale))
                );
    }
}
