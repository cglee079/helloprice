package com.podo.helloprice.core.domain.item;

import java.time.LocalDateTime;

public interface ItemRepositoryCustom {

    Item findOneByLastCrawledBeforePublishAt(ItemStatus itemStatus, LocalDateTime expirePoolAt);
}
