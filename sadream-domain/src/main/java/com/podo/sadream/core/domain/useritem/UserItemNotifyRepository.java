package com.podo.sadream.core.domain.useritem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemNotifyRepository extends JpaRepository<UserItemNotify, Long>, UserItemNotifyRepositoryCustom{

    UserItemNotify findByUserIdAndItemId(Long userId, Long itemId);

    List<UserItemNotify> findByItemId(Long itemId);

    List<UserItemNotify> findByUserId(Long id);
}
