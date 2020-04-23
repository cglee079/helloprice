package com.podo.helloprice.telegram.app.menu;


import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.value.MessageVo;

public interface MenuHandler {

    Menu getHandleMenu();

    TelegramMessageSender sender();

    void handle(MessageVo messageVo, String messageContents);

}
