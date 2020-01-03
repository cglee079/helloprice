package com.podo.helloprice.telegram.client.menu.global;

import com.podo.helloprice.telegram.domain.item.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemCommandTranslator {

    public static List<String> getItemCommands(List<ItemDto.detail> items) {
        return items.stream()
                .map(item -> getItemCommand(item.getItemCode(), item.getItemName()))
                .collect(toList());
    }

    public static String getItemCommand(String itemCode, String itemName) {
        final String FORMAT = "#%s, %s";
        return String.format(FORMAT, itemCode, itemName);
    }

    public static String getItemCodeFromCommand(String message) {
        if (StringUtils.isEmpty(message)) {
            return null;
        }

        int index = message.indexOf(",");

        if (index == -1) {
            return null;
        }

        return message.substring(1, index);
    }
}
