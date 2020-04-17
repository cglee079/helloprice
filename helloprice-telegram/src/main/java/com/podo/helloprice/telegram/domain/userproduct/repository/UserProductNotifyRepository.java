package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductNotifyRepository extends JpaRepository<UserProductNotify, Long>, UserProductNotifyRepositoryCustom{

    UserProductNotify findByUserIdAndProductIdAndPriceType(Long userId, Long productId, PriceType priceType);
}
