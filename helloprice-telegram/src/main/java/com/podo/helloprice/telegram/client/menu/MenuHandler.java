package com.podo.helloprice.telegram.client.menu;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.TelegramBot;

public interface MenuHandler {

    Menu getHandleMenu();

    void handle(TMessageVo tMessageVo, String requestMessage);

    void setBot(TelegramBot telegramBot);
}
