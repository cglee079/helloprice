package com.podo.helloprice.telegram.client.menu;

import com.podo.helloprice.telegram.client.menu.emaildelete.EmailDeleteKeyboard;
import com.podo.helloprice.telegram.client.menu.itemdelete.ItemDeleteKeyboard;
import com.podo.helloprice.telegram.client.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.client.menu.itemsearchresult.ItemSearchResultKeyboard;
import com.podo.helloprice.telegram.client.menu.itemserachadd.ItemSearchAddKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.List;

@Component
public class Keyboard {

    private static ReplyKeyboardRemove DEFAULT_KEYBOARD = new ReplyKeyboardRemove();

    public static ReplyKeyboard getHomeKeyboard(List<String> itemCommands) {
        return new HomeKeyboard(itemCommands);
    }

    public static ReplyKeyboard getDefaultKeyboard() {
        return DEFAULT_KEYBOARD;
    }

    public static ReplyKeyboard getItemDeleteKeyboard(List<String> itemCommands) {
        return new ItemDeleteKeyboard(itemCommands);
    }

    public static ReplyKeyboard getItemSearchResultKeyboard(List<String> itemCommands) {
        return new ItemSearchResultKeyboard(itemCommands);
    }

    public static ReplyKeyboard getItemSearchAddKeyboard(String itemCode) {
        return new ItemSearchAddKeyboard(itemCode);
    }


    public static ReplyKeyboard getEmailDeleteKeyboard() {
        return new EmailDeleteKeyboard();
    }
}
