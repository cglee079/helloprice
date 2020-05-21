package com.podo.helloprice.telegram.domain.tusernotify.repository;

import com.podo.helloprice.telegram.domain.tusernotify.TUserNotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

public interface TUserNotifyRepository extends JpaRepository<TUserNotify, Long>, TUserNotifyRepositoryCustom {

    @Query("SELECT noti FROM TUserNotify noti WHERE noti.tUser.id = :tUserId AND noti.productSale.id = :productSaleId")
    Optional<TUserNotify> findByTUserIdAndProductSaleId(@Param("tUserId") Long tUserId, @Param("productSaleId") Long productSaleId);
}
