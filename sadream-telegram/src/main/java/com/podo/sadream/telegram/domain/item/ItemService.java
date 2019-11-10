package com.podo.sadream.telegram.domain.item;

import com.podo.itemwatcher.pooler.DanawaPooler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public Long merge(ItemInfoVo itemInfoVo) {

        Item item = itemRepository.findByItemCode(itemInfoVo.getItemCode());

        if (Objects.nonNull(item)) {
            item.updateInfo(itemInfoVo, LocalDateTime.now());
            return item.getId();
        }

        final ItemDto.insert itemInsert = ItemDto.insert.builder()
                .itemCode(itemInfoVo.getItemCode())
                .itemUrl(DanawaPooler.DANAWA_ITEM_URL + itemInfoVo.getItemCode())
                .itemName(itemInfoVo.getItemName())
                .itemImage(itemInfoVo.getItemImage())
                .itemPrice(itemInfoVo.getItemPrice())
                .itemSaleStatus(itemInfoVo.getItemSaleStatus())
                .build();

        return this.insert(itemInsert);
    }

    public Long insert(ItemDto.insert itemInsert) {

        Item item = itemRepository.save(itemInsert.toEntity());

        return item.getId();
    }

    public List<ItemDto.detail> findByUserTelegramId(String telegramId) {
        final List<Item> items = itemRepository.findByUserTelegramId(telegramId);

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

    public Long update(Long itemId, ItemInfoVo itemInfoVo) {
        final Item item = itemRepository.findById(itemId).get();

        item.updateInfo(itemInfoVo, LocalDateTime.now());

        return itemId;
    }

    public void cleanInvalidItems() {

        Set<Item> itemSet = new HashSet<>();

        itemSet.addAll(itemRepository.findByItemStatus(ItemStatus.DEAD));
        itemSet.addAll(itemRepository.findByItemSaleStatus(ItemSaleStatus.ERROR));
        itemSet.addAll(itemRepository.findByItemSaleStatus(ItemSaleStatus.DISCONTINUE));
        itemSet.addAll(itemRepository.findByItemSaleStatus(ItemSaleStatus.NOT_SUPPORT));
    }


    public boolean existByItemCode(String itemCode) {
        return Objects.isNull(itemRepository.findByItemCode(itemCode));
    }
}
