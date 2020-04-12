package com.podo.helloprice.telegram.app.menu.productserachadd;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import com.podo.helloprice.telegram.app.menu.global.ProductAddHandler;
import com.podo.helloprice.telegram.app.menu.global.ProductCommandTranslator;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductSearchAddMenuHandler extends AbstractMenuHandler {

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_SEARCH_ADD;
    }

    private final ProductAddHandler productAddHandler;
    private final UserProductNotifyService userProductNotifyService;
    private final SendMessageCallbackFactory callbackFactory;

    public void handle(MessageVo messageVo, String requestMessage) {

        final String telegramId = messageVo.getTelegramId();

        log.debug("APP :: {} << 상품 검색 추가 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> productCommands = ProductCommandTranslator.getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        if (requestMessage.length() < ProductSearchAddCommand.YES.getValue().length()) {
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        final ProductSearchAddCommand requestCommand = ProductSearchAddCommand.from(requestMessage.substring(0, ProductSearchAddCommand.COMMAND_LENGTH));
        if (Objects.isNull(requestCommand)) {
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        handleCommand(requestCommand, messageVo, requestMessage, productCommands);
    }

    private void handleCommand(ProductSearchAddCommand requestCommand, MessageVo messageVo, String requestMessage, List<String> productCommands) {
        switch (requestCommand) {
            case YES:
                final int productCodeValueBeginIndex = ProductSearchAddCommand.COMMAND_LENGTH + 1;
                final String productCode = requestMessage.substring(productCodeValueBeginIndex).replace("#", "");
                productAddHandler.handleProductAdd(messageVo, productCode);
                break;
            case NO:
                sender().send(SendMessageVo.create(messageVo, CommonResponse.toHome(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
        }
    }

}
