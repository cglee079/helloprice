package com.podo.helloprice.telegram.client.menu;

import com.podo.helloprice.telegram.client.TelegramMessageSender;

public abstract class AbstractMenuHandler implements MenuHandler {

    private TelegramMessageSender telegramMessageSender;

    @Override
    public void setSender(TelegramMessageSender telegramMessageSender) {
        this.telegramMessageSender = telegramMessageSender;
    }

    @Override
    public TelegramMessageSender getSender() {
        return telegramMessageSender;
    }

}
