package com.podo.itemwatcher.core.domain.item;

import com.podo.itemwatcher.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
