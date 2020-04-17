package com.podo.helloprice.crawl.scheduler.domain.product;

import com.podo.helloprice.core.model.ProductAliveStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Repository
public class ProductQuerydslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public Product findOneByLastCrawledBeforePublishAt(ProductAliveStatus aliveStatus, LocalDateTime expirePoolAt) {
        return jpaQueryFactory.selectFrom(QProduct.product)
                .where(QProduct.product.aliveStatus.eq(aliveStatus))
                .where(QProduct.product.lastPublishAt.lt(expirePoolAt))
                .orderBy(QProduct.product.lastPublishAt.asc())
                .limit(1)
                .fetchOne();
    }

    public Product findByProductCode(String productCode) {
        return jpaQueryFactory.selectFrom(QProduct.product)
                .where(QProduct.product.productCode.eq(productCode))
                .fetchOne();
    }
}
