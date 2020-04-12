package com.podo.helloprice.core.domain.useritem.repository;

import com.podo.helloprice.core.domain.useritem.UserProductNotify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductNotifyRepository extends JpaRepository<UserProductNotify, Long>, UserProductNotifyRepositoryCustom{

    UserProductNotify findByUserIdAndProductId(Long userId, Long itemId);


    List<UserProductNotify> findByProductId(Long itemId);
}
