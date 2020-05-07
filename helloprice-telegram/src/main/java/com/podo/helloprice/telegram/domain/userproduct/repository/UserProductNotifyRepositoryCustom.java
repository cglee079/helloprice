package com.podo.helloprice.telegram.domain.userproduct.repository;

import com.podo.helloprice.telegram.domain.userproduct.UserProductSaleNotify;

import java.util.List;

public interface UserProductNotifyRepositoryCustom {

    List<UserProductSaleNotify> findByTelegramId(String telegramId);
}
