package com.podo.helloprice.telegram.client.menu.itemsearchresult;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ItemSearchResultKeyboard extends ReplyKeyboardMarkup {


    public ItemSearchResultKeyboard(List<String> itemCommands) {

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        final KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(ItemSearchResultCommand.EXIT.getValue());
        keyboard.add(keyboardRow);

        for (String item : itemCommands) {
            KeyboardRow keyboardItemRow = new KeyboardRow();
            keyboardItemRow.add(item);
            keyboard.add(keyboardItemRow);
        }

        this.setKeyboard(keyboard);
    }

}
