package com.podo.helloprice.core.domain.product.repository;

import com.podo.helloprice.core.domain.product.Product;
import com.podo.helloprice.core.domain.item.model.ProductStatus;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;

import static com.podo.helloprice.core.domain.item.QProduct.item;

public class ProductRepositoryCustomImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {


    public ProductRepositoryCustomImpl() {
        super(Product.class);
    }

    @Override
    public Product findOneByLastCrawledBeforePublishAt(ProductStatus itemStatus, LocalDateTime expirePoolAt) {
        return from(item)
                .where(item.itemStatus.eq(itemStatus))
                .where(item.lastPublishAt.lt(expirePoolAt))
                .orderBy(item.lastPublishAt.asc())
                .limit(1)
                .fetchOne();
    }
}
