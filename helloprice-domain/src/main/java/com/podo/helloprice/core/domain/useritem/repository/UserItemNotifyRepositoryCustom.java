package com.podo.helloprice.core.domain.useritem.repository;

import com.podo.helloprice.core.domain.user.model.UserStatus;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;

import java.util.List;

public interface UserItemNotifyRepositoryCustom {

    List<UserItemNotify> findByUserTelegramId(String telegramId);
    List<UserItemNotify> findByItemIdAndUserStatus(Long itemId, UserStatus userStatus);
}
