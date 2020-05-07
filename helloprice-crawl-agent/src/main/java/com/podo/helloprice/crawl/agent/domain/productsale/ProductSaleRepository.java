package com.podo.helloprice.crawl.agent.domain.productsale;

import com.podo.helloprice.core.enums.SaleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {

    List<ProductSale> findByProductId(Long productId);

    Optional<ProductSale> findByProductIdAndSaleType(Long productId, SaleType saleType);

    @Query("SELECT ps FROM ProductSale ps WHERE  ps.product.productCode = :productCode AND ps.saleType = :saleType")
    Optional<ProductSale>  findByProductCodeAndSaleType(
            @Param("productCode") String productCode, @Param("saleType") SaleType saleType);
}
