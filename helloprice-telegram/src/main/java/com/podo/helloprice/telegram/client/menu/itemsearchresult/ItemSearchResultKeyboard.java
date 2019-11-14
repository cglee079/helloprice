package com.podo.helloprice.telegram.client.menu.itemsearchresult;

import com.podo.helloprice.telegram.client.menu.home.HomeCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.util.ArrayList;
import java.util.List;

public class ItemSearchResultKeyboard extends ReplyKeyboardMarkup {


    public ItemSearchResultKeyboard(List<String> itemCommands) {

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

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
