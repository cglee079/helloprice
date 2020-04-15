package com.podo.helloprice.telegram.app.menu.product.delete;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ProductDeleteKeyboard extends ReplyKeyboardMarkup {

    public ProductDeleteKeyboard(List<String> productList) {

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(ProductDeleteCommand.EXIT.getValue());
        keyboard.add(keyboardRow);

        for (String product : productList) {
            final KeyboardRow keyboardProductRow = new KeyboardRow();
            keyboardProductRow.add(product);
            keyboard.add(keyboardProductRow);
        }

        this.setKeyboard(keyboard);

    }

}
