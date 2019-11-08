package com.podo.itemwatcher.telegram.global.infra.telegram.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Component
public class KeyboardManager {

    private ReplyKeyboardRemove defaultKeyboard = new ReplyKeyboardRemove();

    public ReplyKeyboard getHomeKeyboard() {
        return new HomeKeyboard();
    }

    public ReplyKeyboard getDefaultKeyboard() {
        return defaultKeyboard;
    }
}
