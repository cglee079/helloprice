package com.podo.helloprice.core.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Item findByItemCode(String itemCode);

    List<Item> findByItemStatus(ItemStatus itemStatus);

    List<Item> findByItemSaleStatus(ItemSaleStatus itemSaleStatus);
}
