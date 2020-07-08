package com.podo.helloprice.telegram.domain.tusernotify.repository;

import com.podo.helloprice.telegram.domain.tusernotify.TUserNotify;

import java.util.List;

public interface TUserNotifyRepositoryCustom {

    List<TUserNotify> findByTelegramId(String telegramId);
}
