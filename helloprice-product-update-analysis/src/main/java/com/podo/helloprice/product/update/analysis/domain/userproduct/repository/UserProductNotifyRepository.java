package com.podo.helloprice.product.update.analysis.domain.userproduct.repository;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductNotify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductNotifyRepository extends JpaRepository<UserProductNotify, Long>{
    List<UserProductNotify> findByProductId(Long productId);

    List<UserProductNotify> findByProductIdAndPriceType(Long productId, PriceType priceType);
}
