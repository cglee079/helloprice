package com.podo.helloprice.api.domain.productsale;


import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.product.model.QProduct;
import com.podo.helloprice.api.domain.userproduct.QUserProductSaleNotify;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.podo.helloprice.api.domain.productsale.QProductSale.productSale;
import static com.podo.helloprice.api.domain.userproduct.QUserProductSaleNotify.userProductSaleNotify;


@RequiredArgsConstructor
@Repository
public class ProductSaleQuerydslRepository {

    private final JPAQueryFactory queryFactory;


    public List<ProductSale> findTopDecrease(LocalDateTime now) {

        return null;
    }

    public List<ProductSale> findNotifiedByUserId(Long userId) {
        return queryFactory.select(userProductSaleNotify.productSale)
                .from(userProductSaleNotify)
                .where(userProductSaleNotify.user.id.eq(userId))
                .fetch();
    }

    public Map<Product, List<ProductSale>> findInProducts(List<Product> products) {
        return queryFactory.selectFrom(productSale)
                .leftJoin(productSale.product, QProduct.product).fetchJoin()
                .where(productSale.product.in(products))
                .transform(
                        GroupBy.groupBy(productSale.product)
                                .as(GroupBy.list(productSale))
                );
    }
}
