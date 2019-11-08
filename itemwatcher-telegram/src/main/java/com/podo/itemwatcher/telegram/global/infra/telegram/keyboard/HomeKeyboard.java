package com.podo.itemwatcher.telegram.global.infra.telegram.keyboard;

import com.podo.itemwatcher.telegram.global.infra.telegram.command.HomeCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class HomeKeyboard extends ReplyKeyboardMarkup {

    public HomeKeyboard() {

        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(HomeCommand.ITEM_ADD.getValue());
        keyboardRow.add(HomeCommand.ITEM_DELETE.getValue());

        keyboard.add(keyboardRow);

        this.setKeyboard(keyboard);

    }

}
