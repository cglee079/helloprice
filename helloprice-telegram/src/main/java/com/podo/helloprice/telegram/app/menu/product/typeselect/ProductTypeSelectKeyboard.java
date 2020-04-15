package com.podo.helloprice.telegram.app.menu.product.typeselect;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeSelectKeyboard extends ReplyKeyboardMarkup {

    public ProductTypeSelectKeyboard(List<String> commands) {
        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        for (String command : commands) {
            keyboard.add(getSingleButtonRow(command));
        }

        this.setKeyboard(keyboard);
    }

    private KeyboardRow getSingleButtonRow(String text) {
        final KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(text);
        return keyboardRow;
    }
}
