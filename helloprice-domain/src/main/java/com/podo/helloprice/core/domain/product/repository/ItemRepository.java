package com.podo.helloprice.core.domain.product.repository;

import com.podo.helloprice.core.domain.product.Product;
import com.podo.helloprice.core.domain.item.model.ProductStatus;
import com.podo.helloprice.core.domain.item.model.ProductUpdateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Product findByProductCode(String itemCode);

    List<Product> findByProductStatusAndProductUpdateStatus(ProductStatus itemStatus, ProductUpdateStatus itemUpdateStatus);
}
