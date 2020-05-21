package com.podo.helloprice.telegram.app.menu;

import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.menu.product.global.ProductDescCommandTranslator;
import com.podo.helloprice.telegram.domain.tusernotify.application.TUserNotifyReadService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractMenuHandler implements MenuHandler {

    @Autowired
    private TelegramMessageSender telegramMessageSender;

    @Autowired
    private TUserNotifyReadService tUserNotifyReadService;

    @Override
    public TelegramMessageSender sender() {
        return telegramMessageSender;
    }

    public HomeKeyboard createHomeKeyboard(String telegramId){
        final List<String> productCommands = ProductDescCommandTranslator.encodes(tUserNotifyReadService.findByTelegramId(telegramId));
        return new HomeKeyboard(productCommands);
    }
}
