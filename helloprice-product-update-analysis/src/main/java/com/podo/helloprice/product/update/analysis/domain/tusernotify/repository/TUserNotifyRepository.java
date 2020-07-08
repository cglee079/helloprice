package com.podo.helloprice.product.update.analysis.domain.tusernotify.repository;

import com.podo.helloprice.product.update.analysis.domain.tusernotify.TUserNotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TUserNotifyRepository extends JpaRepository<TUserNotify, Long>{

    @Query("SELECT upsn FROM TUserNotify upsn where upsn.productSale.product.id = :productId")
    List<TUserNotify> findByProductId(@Param("productId") Long productId);

    List<TUserNotify> findByProductSaleId(Long productSaleId);
}
