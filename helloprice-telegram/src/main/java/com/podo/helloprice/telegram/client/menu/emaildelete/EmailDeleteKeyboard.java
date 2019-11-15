package com.podo.helloprice.telegram.client.menu.emaildelete;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class EmailDeleteKeyboard extends ReplyKeyboardMarkup {


    public EmailDeleteKeyboard() {
        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(EmailDeleteCommand.YES.getValue());
        keyboardRow.add(EmailDeleteCommand.NO.getValue());

        keyboard.add(keyboardRow);

        this.setKeyboard(keyboard);

    }
}
