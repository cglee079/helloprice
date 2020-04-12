package com.podo.helloprice.telegram.app.menu;

import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMenuHandler implements MenuHandler {

    @Autowired
    private TelegramMessageSender telegramMessageSender;

    @Override
    public TelegramMessageSender sender() {
        return telegramMessageSender;
    }

}
