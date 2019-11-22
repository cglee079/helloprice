package com.podo.helloprice.telegram.domain.item;

import com.podo.helloprice.core.domain.item.*;
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

    public Long writeCrawledItem(CrawledItemVo crawledItemVo) {

        final Item existedItem = itemRepository.findByItemCode(crawledItemVo.getItemCode());

        if (Objects.nonNull(existedItem)) {
            existedItem.updateByCrawledItem(crawledItemVo, LocalDateTime.now());
            return existedItem.getId();
        }

        final ItemDto.insert itemInsert = ItemDto.insert.builder()
                .itemCode(crawledItemVo.getItemCode())
                .itemUrl(crawledItemVo.getItemUrl())
                .itemName(crawledItemVo.getItemName())
                .itemDesc(crawledItemVo.getItemDesc())
                .itemImage(crawledItemVo.getItemImage())
                .itemPrice(crawledItemVo.getItemPrice())
                .itemSaleStatus(crawledItemVo.getItemSaleStatus())
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
