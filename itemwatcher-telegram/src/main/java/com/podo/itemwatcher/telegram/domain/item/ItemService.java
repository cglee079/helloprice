package com.podo.itemwatcher.telegram.domain.item;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.ItemRepository;
import com.podo.itemwatcher.core.domain.item.ItemSaleStatus;
import com.podo.itemwatcher.core.domain.item.ItemStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public Long insertIfNotExist(ItemDto.insert itemInsert) {

        Item item = itemRepository.findByItemCode(itemInsert.getItemCode());
        if (Objects.nonNull(item)) {
//            item.updateInfo(, LocalDateTime.now());
            return item.getId();
        }

        item = itemRepository.save(itemInsert.toEntity());

        return item.getId();
    }

    public List<ItemDto.detail> findByUserTelegramId(Integer telegramId) {
        final List<Item> items = itemRepository.findByUserTelegramId(telegramId + "");

        return items.stream()
                .map(ItemDto.detail::new)
                .collect(toList());
    }

    public ItemDto.detail findByItemCode(String itemCode) {
        final Item item = itemRepository.findByItemCode(itemCode);

        if (Objects.isNull(item)) {
            return null;
        }

        return new ItemDto.detail(item);
    }

    public ItemDto.detail findByItemId(Long itemId) {
        final Item item = itemRepository.findById(itemId).get();
        return new ItemDto.detail(item);
    }

    public void cleanInvalidItems() {

        Set<Item> itemSet = new HashSet<>();

        itemSet.addAll(itemRepository.findByItemStatus(ItemStatus.DEAD));
        itemSet.addAll(itemRepository.findByItemSaleStatus(ItemSaleStatus.ERROR));
        itemSet.addAll(itemRepository.findByItemSaleStatus(ItemSaleStatus.DISCONTINUE));
        itemSet.addAll(itemRepository.findByItemSaleStatus(ItemSaleStatus.NO_SUPPORT));
    }
}
