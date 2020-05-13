package com.podo.helloprice.telegram.domain.usernotify.repository;

import com.podo.helloprice.telegram.domain.usernotify.UserNotify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductNotifyRepository extends JpaRepository<UserNotify, Long>, UserProductNotifyRepositoryCustom{

    UserNotify findByUserIdAndProductSaleId(Long userId, Long productSaleId);
}
