package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductNotifyRepository extends JpaRepository<UserProductNotify, Long>, UserProductNotifyRepositoryCustom{

    UserProductNotify findByUserIdAndProductId(Long userId, Long productId);

    List<UserProductNotify> findByProductId(Long productId);
}
