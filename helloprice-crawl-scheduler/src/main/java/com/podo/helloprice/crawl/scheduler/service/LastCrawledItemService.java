package com.podo.helloprice.crawl.scheduler.service;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.ItemRepository;
import com.podo.helloprice.core.domain.item.ItemStatus;
import com.podo.helloprice.crawl.core.vo.LastCrawledItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class LastCrawledItemService {

    private final ItemRepository itemRepository;

    public LastCrawledItem getLastCrawledItem(LocalDateTime expirePublishAt){
        final Item item = itemRepository.findOneByLastCrawledBeforePublishAt(ItemStatus.ALIVE, expirePublishAt);

        if(Objects.isNull(item)){
            return null;
        }

        return new LastCrawledItem(item.getItemName(), item.getItemCode());

    }

    public void updateLastPublishAt(String itemCode, LocalDateTime lastPublishAt) {
        final Item item = itemRepository.findByItemCode(itemCode);
        item.updateLastPublishAt(lastPublishAt);
    }
}
