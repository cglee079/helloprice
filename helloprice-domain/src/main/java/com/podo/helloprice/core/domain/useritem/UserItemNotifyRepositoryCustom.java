package com.podo.helloprice.core.domain.useritem;

import com.podo.helloprice.core.domain.user.UserStatus;

import java.util.List;

public interface UserItemNotifyRepositoryCustom {

    List<UserItemNotify> findByUserTelegramId(String telegramId);
    List<UserItemNotify> findByItemIdAndUserStatus(Long itemId, UserStatus userStatus);
}
