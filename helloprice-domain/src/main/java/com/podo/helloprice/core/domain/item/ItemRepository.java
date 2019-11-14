package com.podo.helloprice.core.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Item findByItemCode(String itemCode);

    List<Item> findByItemUpdateStatus(ItemUpdateStatus itemUpdateStatus);

    List<Item> findByItemStatusAndItemUpdateStatus(ItemStatus itemStatus, ItemUpdateStatus itemUpdateStatus);
}
