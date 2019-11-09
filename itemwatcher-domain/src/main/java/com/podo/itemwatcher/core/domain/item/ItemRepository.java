package com.podo.itemwatcher.core.domain.item;

import com.podo.itemwatcher.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Item findByItemCode(String itemCode);

    List<Item> findByItemStatus(ItemStatus itemStatus);

    List<Item> findByItemSaleStatus(ItemSaleStatus itemSaleStatus);
}
