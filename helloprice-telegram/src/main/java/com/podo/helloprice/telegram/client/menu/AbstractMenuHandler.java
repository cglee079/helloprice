package com.podo.helloprice.telegram.client.menu;

import com.podo.helloprice.telegram.client.core.TelegramMessageSender;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMenuHandler implements MenuHandler {

    @Autowired
    private TelegramMessageSender telegramMessageSender;

    @Override
    public TelegramMessageSender sender() {
        return telegramMessageSender;
    }

}
