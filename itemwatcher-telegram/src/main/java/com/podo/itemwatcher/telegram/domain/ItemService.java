package com.podo.itemwatcher.telegram.domain;

import com.podo.itemwatcher.core.domain.item.ItemRepository;
import com.podo.itemwatcher.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
}
