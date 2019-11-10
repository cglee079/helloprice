package com.podo.sadream.core.domain.useritem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserItemNotifyRepository extends JpaRepository<UserItemNotify, Long>, UserItemNotifyRepositoryCustom{

    UserItemNotify findByUserIdAndItemId(Long userId, Long itemId);

}
