package com.podo.helloprice.core.domain.product.repository;

import com.podo.helloprice.core.domain.product.Product;
import com.podo.helloprice.core.domain.item.model.ProductStatus;

import java.time.LocalDateTime;

public interface ProductRepositoryCustom {

    Product findOneByLastCrawledBeforePublishAt(ProductStatus itemStatus, LocalDateTime expirePoolAt);
}
