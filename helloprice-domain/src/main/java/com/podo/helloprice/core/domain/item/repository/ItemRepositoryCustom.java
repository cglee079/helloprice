package com.podo.helloprice.core.domain.item.repository;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.model.ItemStatus;

import java.time.LocalDateTime;

public interface ItemRepositoryCustom {

    Item findOneByLastCrawledBeforePublishAt(ItemStatus itemStatus, LocalDateTime expirePoolAt);
}
