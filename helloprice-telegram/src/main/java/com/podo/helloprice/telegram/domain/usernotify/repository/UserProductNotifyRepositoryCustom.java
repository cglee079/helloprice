package com.podo.helloprice.telegram.domain.usernotify.repository;

import com.podo.helloprice.telegram.domain.usernotify.UserNotify;

import java.util.List;

public interface UserProductNotifyRepositoryCustom {

    List<UserNotify> findByTelegramId(String telegramId);
}
