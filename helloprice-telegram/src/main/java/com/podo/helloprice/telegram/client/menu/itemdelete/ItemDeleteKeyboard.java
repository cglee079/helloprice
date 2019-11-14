package com.podo.helloprice.telegram.client.menu.itemdelete;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ItemDeleteKeyboard extends ReplyKeyboardMarkup {

    public ItemDeleteKeyboard(List<String> itemList) {

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(ItemDeleteCommand.EXIT.getValue());

        keyboard.add(keyboardRow);

        for (String item : itemList) {
            KeyboardRow keyboardItemRow = new KeyboardRow();
            keyboardItemRow.add(item);
            keyboard.add(keyboardItemRow);
        }

        this.setKeyboard(keyboard);

    }

}
