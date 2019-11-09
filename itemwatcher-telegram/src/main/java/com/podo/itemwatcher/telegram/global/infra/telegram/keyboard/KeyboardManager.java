package com.podo.itemwatcher.telegram.global.infra.telegram.keyboard;

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
}
