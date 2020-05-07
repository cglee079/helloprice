package com.podo.helloprice.product.update.analysis.domain.userproduct.repository;

import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductSaleNotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProductSaleNotifyRepository extends JpaRepository<UserProductSaleNotify, Long>{

    @Query("SELECT upsn FROM UserProductSaleNotify upsn where upsn.productSale.product.id = :productId")
    List<UserProductSaleNotify> findByProductId(@Param("productId") Long productId);

    List<UserProductSaleNotify> findByProductSaleId(Long productSaleId);
}
