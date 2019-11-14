package com.podo.helloprice.telegram.client.menu.itemserachadd;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ItemSearchAddKeyboard extends ReplyKeyboardMarkup {


    public ItemSearchAddKeyboard(String itemCode) {
        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(ItemSearchAddCommand.YES.getValue() + "#" + itemCode);
        keyboardRow.add(ItemSearchAddCommand.NO.getValue());

        keyboard.add(keyboardRow);

        this.setKeyboard(keyboard);

    }
}
