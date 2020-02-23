package com.podo.helloprice.telegram.domain.item;

import com.podo.helloprice.core.domain.item.*;
import com.podo.helloprice.core.domain.item.model.ItemStatus;
import com.podo.helloprice.core.domain.item.model.ItemUpdateStatus;
import com.podo.helloprice.core.domain.item.repository.ItemRepository;
import com.podo.helloprice.core.domain.item.vo.CrawledItem;
import com.podo.helloprice.telegram.domain.item.exception.InvalidItemIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public Long writeCrawledItem(CrawledItem crawledItem) {

        final Item existedItem = itemRepository.findByItemCode(crawledItem.getItemCode());

        if (Objects.nonNull(existedItem)) {
            existedItem.updateByCrawledItem(crawledItem);
            return existedItem.getId();
        }

        final ItemDto.insert itemInsert = ItemDto.insert.builder()
                .itemCode(crawledItem.getItemCode())
                .itemUrl(crawledItem.getItemUrl())
                .itemName(crawledItem.getItemName())
                .itemDesc(crawledItem.getItemDesc())
                .itemImage(crawledItem.getItemImage())
                .itemPrice(crawledItem.getItemPrice())
                .itemSaleStatus(crawledItem.getItemSaleStatus())
                .build();

        return insertNewItem(itemInsert);
    }

    public Long insertNewItem(ItemDto.insert itemInsert) {
        final Item newItem = itemInsert.toEntity();
        final Item savedItem = itemRepository.save(newItem);
        return savedItem.getId();
    }

    public ItemDto.detail findByItemCode(String itemCode) {
        final Item existedItem = itemRepository.findByItemCode(itemCode);

        if (Objects.isNull(existedItem)) {
            return null;
        }

        return new ItemDto.detail(existedItem);
    }

    public ItemDto.detail findByItemId(Long itemId) {
        final Optional<Item> existedItemOptional = itemRepository.findById(itemId);

        if (!existedItemOptional.isPresent()) {
            throw new InvalidItemIdException(itemId);
        }

        return new ItemDto.detail(existedItemOptional.get());
    }

    public boolean isExistedByItemCode(String itemCode) {
        final Item existedItem = itemRepository.findByItemCode(itemCode);
        return Objects.nonNull(existedItem);
    }

    public void notifiedItem(Long itemId) {
        final Optional<Item> existedItemOptional = itemRepository.findById(itemId);

        if (!existedItemOptional.isPresent()) {
            throw new InvalidItemIdException(itemId);
        }

        final Item existedItem = existedItemOptional.get();
        existedItem.notified();
    }

    public List<ItemDto.detail> findByStatusAndUpdateStatus(ItemStatus itemStatus, ItemUpdateStatus itemUpdateStatus) {
        final List<Item> items = itemRepository.findByItemStatusAndItemUpdateStatus(itemStatus, itemUpdateStatus);

        return items.stream()
                .map(ItemDto.detail::new)
                .collect(Collectors.toList());
    }
}
