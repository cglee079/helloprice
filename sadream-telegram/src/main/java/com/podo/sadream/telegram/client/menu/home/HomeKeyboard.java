package com.podo.sadream.telegram.client.menu.home;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class HomeKeyboard extends ReplyKeyboardMarkup {

    public HomeKeyboard(List<String> itemList) {

        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(HomeCommand.ADD_ITEM.getValue());
        keyboardRow.add(HomeCommand.DELETE_ITEM.getValue());

        keyboard.add(keyboardRow);

        for (String item : itemList) {
            KeyboardRow keyboardItemRow = new KeyboardRow();
            keyboardItemRow.add(item);
            keyboard.add(keyboardItemRow);
        }

        this.setKeyboard(keyboard);

    }

}
