package com.podo.helloprice.telegram.app.menu;

import com.podo.helloprice.telegram.app.menu.emaildelete.EmailDeleteKeyboard;
import com.podo.helloprice.telegram.app.menu.productdelete.ProductDeleteKeyboard;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.menu.productsearchresult.ProductSearchResultKeyboard;
import com.podo.helloprice.telegram.app.menu.productserachadd.ProductSearchAddKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.List;

@Component
public class Keyboard {

    private static ReplyKeyboardRemove DEFAULT_KEYBOARD = new ReplyKeyboardRemove();

    public static ReplyKeyboard getHomeKeyboard(List<String> productCommands) {
        return new HomeKeyboard(productCommands);
    }

    public static ReplyKeyboard getDefaultKeyboard() {
        return DEFAULT_KEYBOARD;
    }

    public static ReplyKeyboard getProductDeleteKeyboard(List<String> productCommands) {
        return new ProductDeleteKeyboard(productCommands);
    }

    public static ReplyKeyboard getProductSearchResultKeyboard(List<String> productCommands) {
        return new ProductSearchResultKeyboard(productCommands);
    }

    public static ReplyKeyboard getProductSearchAddKeyboard(String productCode) {
        return new ProductSearchAddKeyboard(productCode);
    }


    public static ReplyKeyboard getEmailDeleteKeyboard() {
        return new EmailDeleteKeyboard();
    }
}
