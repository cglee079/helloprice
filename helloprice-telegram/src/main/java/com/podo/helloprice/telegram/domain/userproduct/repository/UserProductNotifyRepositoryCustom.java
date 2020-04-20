package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.core.model.UserStatus;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;

import java.util.List;

public interface UserProductNotifyRepositoryCustom {

    List<UserProductNotify> findByTelegramId(String telegramId);
}
