package com.podo.helloprice.telegram.app.menu.product.searchselect;

import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.KeyboardHelper;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.product.*;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import com.podo.helloprice.telegram.global.cache.DanawaProductCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductSearchSelectMenuHandler extends AbstractMenuHandler {

    @Override
    public Menu getHandleMenu() {
        return Menu.PRODUCT_SEARCH_RESULT;
    }

    private final ProductAddHandler productAddHandler;
    private final DanawaProductCache danawaProductCache;
    private final UserProductNotifyService userProductNotifyService;
    private final SendMessageCallbackFactory callbackFactory;

    public void handle(MessageVo messageVo, String requestMessage) {
        final String telegramId = messageVo.getTelegramId();
        final List<String> productCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        log.debug("APP :: {} << 상품 검색 결과 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final ProductSearchSelectCommand requestCommand = ProductSearchSelectCommand.from(requestMessage);
        if (Objects.nonNull(requestCommand)) {
            handleCommand(requestCommand, messageVo, productCommands);
            return;
        }

        final String productCode = ProductSearchCommandTranslator.decode(requestMessage);

        if (Objects.isNull(productCode)) {
            log.debug("APP :: {} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.wrongInput(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }


        handleProductCommand(productCode, messageVo);
    }

    private void handleCommand(ProductSearchSelectCommand requestCommand, MessageVo messageVo, List<String> productCommands) {
        switch (requestCommand) {
            case EXIT:
                sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.toHome(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
        }
    }

    private void handleProductCommand(String productCode, MessageVo messageVo) {
        final String telegramId = messageVo.getTelegramId();

        sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        productAddHandler.handleProductAdd(messageVo, productCode);
    }

}
