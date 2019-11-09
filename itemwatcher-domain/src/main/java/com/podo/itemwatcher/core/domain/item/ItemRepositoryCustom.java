package com.podo.itemwatcher.core.domain.item;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> findByUserTelegramId(String telegramId);
}
