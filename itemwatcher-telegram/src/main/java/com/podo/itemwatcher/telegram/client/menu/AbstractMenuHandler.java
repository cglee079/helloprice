package com.podo.itemwatcher.telegram.client.menu;

import com.podo.itemwatcher.telegram.client.TelegramBot;

public abstract class AbstractMenuHandler implements MenuHandler {

    private TelegramBot telegramBot;

    @Override
    public final void setBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public TelegramBot getBot() {
        return telegramBot;
    }

}
