package com.podo.helloprice.telegram.app.menu;

import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.menu.product.global.ProductDescCommandTranslator;
import com.podo.helloprice.telegram.domain.userproduct.application.UserProductNotifyReadService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractMenuHandler implements MenuHandler {

    @Autowired
    private TelegramMessageSender telegramMessageSender;

    @Autowired
    private UserProductNotifyReadService userProductNotifyReadService;

    @Override
    public TelegramMessageSender sender() {
        return telegramMessageSender;
    }

    public HomeKeyboard getHomeKeyboard(String telegramId){
        final List<String> productCommands = ProductDescCommandTranslator.encodes(userProductNotifyReadService.findNotifyProductsByTelegramId(telegramId));
        return new HomeKeyboard(productCommands);
    }
}
