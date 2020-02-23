package com.podo.helloprice.telegram.client.menu;

import com.podo.helloprice.core.model.Menu;
import com.podo.helloprice.telegram.client.vo.TMessageVo;
import com.podo.helloprice.telegram.client.core.TelegramMessageSender;

public interface MenuHandler {

    Menu getHandleMenu();

    TelegramMessageSender sender();

    void handle(TMessageVo tMessageVo, String requestMessage);

}
