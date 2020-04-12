package com.podo.helloprice.crawl.scheduler.domain.product;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.podo.helloprice.crawl.scheduler.domain.product.QProduct.product;


@RequiredArgsConstructor
@Repository
public class ProductQuerydslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public Product findOneByLastCrawledBeforePublishAt(ProductAliveStatus aliveStatus, LocalDateTime expirePoolAt) {
        return jpaQueryFactory.selectFrom(product)
                .where(product.aliveStatus.eq(aliveStatus))
                .where(product.lastPublishAt.lt(expirePoolAt))
                .orderBy(product.lastPublishAt.asc())
                .limit(1)
                .fetchOne();
    }

    public Product findByProductCode(String productCode) {
        return jpaQueryFactory.selectFrom(product)
                .where(product.productCode.eq(productCode))
                .fetchOne();
    }
}
