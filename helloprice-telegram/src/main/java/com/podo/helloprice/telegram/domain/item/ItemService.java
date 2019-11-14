package com.podo.helloprice.telegram.domain.item;

import com.podo.helloprice.core.domain.item.*;
import com.podo.helloprice.core.domain.useritem.UserItemNotifyRepository;
import com.podo.helloprice.pooler.target.danawa.DanawaPoolConfig;
import com.podo.helloprice.pooler.target.danawa.DanawaPooler;
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
    private final UserItemNotifyRepository userItemNotifyRepository;

    public Long merge(ItemInfoVo itemInfoVo) {

        Item item = itemRepository.findByItemCode(itemInfoVo.getItemCode());

        if (Objects.nonNull(item)) {
            item.updateInfo(itemInfoVo, LocalDateTime.now());
            return item.getId();
        }

        final ItemDto.insert itemInsert = ItemDto.insert.builder()
                .itemCode(itemInfoVo.getItemCode())
                .itemUrl(itemInfoVo.getItemUrl())
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

    public boolean existByItemCode(String itemCode) {
        return Objects.isNull(itemRepository.findByItemCode(itemCode));
    }

    public List<ItemDto.detail> findByItemUpdateStatus(ItemUpdateStatus itemUpdateStatus) {
        List<Item> items = itemRepository.findByItemUpdateStatus(itemUpdateStatus);
        return items.stream()
                .map(ItemDto.detail::new)
                .collect(Collectors.toList());
    }

    public void deleteByItemId(Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        if (itemOptional.isPresent()) {

            Item item = itemOptional.get();
            userItemNotifyRepository.deleteAll(item.getUserItemNotifies());

            itemRepository.delete(item);
        }


    }

    public void notifiedItem(Long itemId) {
        Item item = itemRepository.findById(itemId).get();
        item.notified();
    }

    public List<ItemDto.detail> findByItemStatusAndItemUpdateStatus(ItemStatus itemStatus, ItemUpdateStatus itemUpdateStatus) {
        List<Item> items = itemRepository.findByItemStatusAndItemUpdateStatus(itemStatus, itemUpdateStatus);
        return items.stream()
                .map(ItemDto.detail::new)
                .collect(Collectors.toList());
    }
}
