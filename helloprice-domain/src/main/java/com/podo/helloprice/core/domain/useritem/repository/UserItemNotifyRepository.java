package com.podo.helloprice.core.domain.useritem.repository;

import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemNotifyRepository extends JpaRepository<UserItemNotify, Long>, UserItemNotifyRepositoryCustom{

    UserItemNotify findByUserIdAndItemId(Long userId, Long itemId);


    List<UserItemNotify> findByItemId(Long itemId);
}
