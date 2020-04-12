package com.podo.helloprice.telegram.app.menu.productserachadd;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchAddKeyboard extends ReplyKeyboardMarkup {

    public ProductSearchAddKeyboard(String productCode) {
        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        final KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(ProductSearchAddCommand.YES.getValue() + "#" + productCode);
        keyboardRow.add(ProductSearchAddCommand.NO.getValue());

        keyboard.add(keyboardRow);

        this.setKeyboard(keyboard);

    }
}
