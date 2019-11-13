package com.podo.helloprice.telegram.client.menu;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.TelegramMessageSender;

public interface MenuHandler {

    Menu getHandleMenu();

    void handle(TMessageVo tMessageVo, String requestMessage);

    void setSender(TelegramMessageSender telegramMessageSender);

    TelegramMessageSender getSender();
}
