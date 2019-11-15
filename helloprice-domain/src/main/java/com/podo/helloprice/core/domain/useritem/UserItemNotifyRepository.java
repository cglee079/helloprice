package com.podo.helloprice.core.domain.useritem;

import com.podo.helloprice.core.domain.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemNotifyRepository extends JpaRepository<UserItemNotify, Long>, UserItemNotifyRepositoryCustom{

    UserItemNotify findByUserIdAndItemId(Long userId, Long itemId);


    List<UserItemNotify> findByItemId(Long itemId);
}
