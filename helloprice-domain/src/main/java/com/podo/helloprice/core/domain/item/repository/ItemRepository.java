package com.podo.helloprice.core.domain.item.repository;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.model.ItemStatus;
import com.podo.helloprice.core.domain.item.model.ItemUpdateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Item findByItemCode(String itemCode);

    List<Item> findByItemStatusAndItemUpdateStatus(ItemStatus itemStatus, ItemUpdateStatus itemUpdateStatus);
}
