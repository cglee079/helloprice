package com.podo.helloprice.core.domain.useritem.repository;

import com.podo.helloprice.core.domain.user.model.UserStatus;
import com.podo.helloprice.core.domain.useritem.UserProductNotify;

import java.util.List;

public interface UserProductNotifyRepositoryCustom {

    List<UserProductNotify> findByUserTelegramId(String telegramId);
    List<UserProductNotify> findByProductIdAndUserStatus(Long itemId, UserStatus userStatus);
}
