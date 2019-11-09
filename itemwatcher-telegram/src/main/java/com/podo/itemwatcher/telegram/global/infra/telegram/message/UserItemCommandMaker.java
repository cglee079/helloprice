package com.podo.itemwatcher.telegram.global.infra.telegram.message;

import com.podo.itemwatcher.telegram.domain.item.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserItemCommandMaker {

    private static final String FORMAT = "#%s, %s";

    private String formatCommand(String itemCode, String itemName) {

        return String.format(FORMAT, itemCode, itemName);
    }

    public List<String> getItemCommands(List<ItemDto.detail> items) {
        return items.stream()
                .map(item -> formatCommand(item.getItemCode(), item.getItemName()))
                .collect(toList());
    }

    public String getItemCodeFromCommand(String message) {
        int index = message.indexOf(",");

        if (index == -1) {
            return null;
        }

        return message.substring(1, index);
    }
}
