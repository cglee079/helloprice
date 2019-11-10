package com.podo.sadream.core.domain.useritem;

import com.podo.sadream.core.domain.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemNotifyRepository extends JpaRepository<UserItemNotify, Long>, UserItemNotifyRepositoryCustom{

    UserItemNotify findByUserIdAndItemId(Long userId, Long itemId);


}
