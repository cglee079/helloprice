package com.podo.helloprice.telegram.app.menu;


import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.domain.user.model.Menu;

public interface MenuHandler {

    Menu getHandleMenu();

    TelegramMessageSender sender();

    void handle(MessageVo messageVo, String messageContents);

}
