package com.podo.helloprice.telegram.client.menu.home;

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

        final List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(HomeCommand.ITEM_SEARCH_ADD.getValue());
        keyboardRow.add(HomeCommand.ITEM_ADD.getValue());
        keyboardRow.add(HomeCommand.ITEM_DELETE.getValue());
        keyboard.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(HomeCommand.EMAIL_ADD.getValue());
        keyboardRow.add(HomeCommand.EMAIL_DELETE.getValue());
        keyboardRow.add(HomeCommand.HELP.getValue());
        keyboard.add(keyboardRow);


        for (String item : itemList) {
            final KeyboardRow keyboardItemRow = new KeyboardRow();
            keyboardItemRow.add(item);
            keyboard.add(keyboardItemRow);
        }

        this.setKeyboard(keyboard);

    }

}
