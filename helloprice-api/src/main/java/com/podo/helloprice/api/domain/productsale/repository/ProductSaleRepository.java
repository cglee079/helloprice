package com.podo.helloprice.api.domain.productsale.repository;

import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.core.enums.SaleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {
    Optional<ProductSale> findByProductIdAndSaleType(Long productId, SaleType saleType);

    List<ProductSale> findByIdIn(List<Long> productSaleIds);

    List<ProductSale> findByProductId(Long productId);
}
