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
public class KeyboardManager {

    private ReplyKeyboardRemove defaultKeyboard = new ReplyKeyboardRemove();

    public ReplyKeyboard getHomeKeyboard(List<String> itemList) {
        return new HomeKeyboard(itemList);
    }

    public ReplyKeyboard getDefaultKeyboard() {
        return defaultKeyboard;
    }

    public ReplyKeyboard getItemDeleteKeyboard(List<String> itemCommands) {
        return new ItemDeleteKeyboard(itemCommands);
    }

    public ReplyKeyboard getItemSearchResultKeyboard(List<String> itemCommands) {
        return new ItemSearchResultKeyboard(itemCommands);
    }

    public ReplyKeyboard getItemSearchAddKeyboard(String itemCode) {
        return new ItemSearchAddKeyboard(itemCode);
    }


    public ReplyKeyboard getEmailDeleteKeyboard() {
        return new EmailDeleteKeyboard();
    }
}
