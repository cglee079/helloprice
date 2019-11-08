package com.podo.itemwatcher.telegram.domain;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.ItemRepository;
import com.podo.itemwatcher.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public Long insertIfNotExist(ItemDto.insert itemInsert) {

        Item item = itemRepository.findByItemCode(itemInsert.getItemCode());
        if (Objects.nonNull(item)) {
            item.updateInfo(itemInsert.getItemName(), itemInsert.getItemImage(), itemInsert.getItemPrice(), LocalDateTime.now());
            return item.getId();
        }

        item = itemRepository.save(itemInsert.toEntity());

        return item.getId();
    }
}
