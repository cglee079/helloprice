package com.podo.helloprice.telegram.app.menu.productsearchresult;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchResultKeyboard extends ReplyKeyboardMarkup {


    public ProductSearchResultKeyboard(List<String> productCommands) {

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        final KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(ProductSearchResultCommand.EXIT.getValue());
        keyboard.add(keyboardRow);

        for (String product : productCommands) {
            KeyboardRow keyboardProductRow = new KeyboardRow();
            keyboardProductRow.add(product);
            keyboard.add(keyboardProductRow);
        }

        this.setKeyboard(keyboard);
    }

}
