package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.telegram.domain.userproduct.UserProductSaleNotify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductNotifyRepository extends JpaRepository<UserProductSaleNotify, Long>, UserProductNotifyRepositoryCustom{

    UserProductSaleNotify findByUserIdAndProductSaleId(Long userId, Long productSaleId);
}
