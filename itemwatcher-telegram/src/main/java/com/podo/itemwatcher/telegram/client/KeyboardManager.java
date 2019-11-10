package com.podo.itemwatcher.telegram.client;

import com.podo.itemwatcher.telegram.client.menu.itemdelete.ItemDeleteKeyboard;
import com.podo.itemwatcher.telegram.client.menu.home.HomeKeyboard;
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
}
