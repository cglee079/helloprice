package com.podo.sadream.telegram.client.menu;

import com.podo.itemwatcher.core.domain.user.Menu;
import com.podo.sadream.telegram.client.TMessageVo;
import com.podo.sadream.telegram.client.TelegramBot;

public interface MenuHandler {

    Menu getHandleMenu();

    void handle(TMessageVo tMessageVo, String requestMessage);

    void setBot(TelegramBot telegramBot);
}
