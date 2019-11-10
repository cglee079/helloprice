package com.podo.sadream.telegram.job;

import com.podo.sadream.telegram.domain.item.ItemService;
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
