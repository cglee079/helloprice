package com.podo.helloprice.product.update.analysis.domain.userproduct.repository;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductSaleNotify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductNotifyRepository extends JpaRepository<UserProductSaleNotify, Long>{

    List<UserProductSaleNotify> findByProductId(Long productId);

    List<UserProductSaleNotify> findByProductIdAndPriceType(Long productId, SaleType saleType);
}
