package com.podo.sadream.core.domain.useritem;

import com.podo.sadream.core.domain.user.UserStatus;

import java.util.List;

public interface UserItemNotifyRepositoryCustom {

    List<UserItemNotify> findByUserTelegramId(String telegramId);
    List<UserItemNotify> findByItemIdAndUserStatus(Long itemId, UserStatus userStatus);
}
