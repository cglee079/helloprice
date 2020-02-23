package com.podo.helloprice.crawl.scheduler.service;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.repository.ItemRepository;
import com.podo.helloprice.core.domain.item.model.ItemStatus;
import com.podo.helloprice.crawl.core.vo.LastPublishedItem;
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

    public LastPublishedItem getLastCrawledItem(LocalDateTime expirePublishAt){
        final Item item = itemRepository.findOneByLastCrawledBeforePublishAt(ItemStatus.ALIVE, expirePublishAt);

        if(Objects.isNull(item)){
            return null;
        }

        return new LastPublishedItem(item.getItemName(), item.getItemCode());

    }

    public void updateLastPublishAt(String itemCode, LocalDateTime lastPublishAt) {
        final Item item = itemRepository.findByItemCode(itemCode);
        item.updateLastPublishAt(lastPublishAt);
    }
}
