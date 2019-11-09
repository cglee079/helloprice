package com.podo.itemwatcher.telegram.job;

import com.podo.itemwatcher.telegram.domain.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CleanWorker implements Worker{

    private final ItemService itemService;

    @Override
    public void doIt() {
        itemService.cleanInvalidItems();
    }
}
