package com.podo.helloprice.telegram.app.menu.productsearchresult;

import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import com.podo.helloprice.telegram.app.menu.global.ProductCommandTranslator;
import com.podo.helloprice.telegram.app.menu.productserachadd.ProductSearchAddResponse;
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
public class ProductSearchResultMenuHandler extends AbstractMenuHandler {

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_SEARCH_RESULT;
    }

    private final DanawaProductCache danawaProductCache;
    private final UserProductNotifyService userProductNotifyService;
    private final SendMessageCallbackFactory callbackFactory;

    public void handle(MessageVo messageVo, String requestMessage) {
        final String telegramId = messageVo.getTelegramId();
        final List<String> productCommands = ProductCommandTranslator.getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        log.debug("APP :: {} << 상품 검색 결과 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final ProductSearchResultCommand requestCommand = ProductSearchResultCommand.from(requestMessage);
        if (Objects.nonNull(requestCommand)) {
            handleCommand(requestCommand, messageVo, productCommands);
            return;
        }

        final String productCode = ProductCommandTranslator.getProductCodeFromCommand(requestMessage);
        if (Objects.isNull(productCode)) {
            log.debug("APP :: {} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }


        handleProductCommand(productCode, messageVo, productCommands);
    }

    private void handleCommand(ProductSearchResultCommand requestCommand, MessageVo messageVo, List<String> productCommands) {
        switch (requestCommand) {
            case EXIT:
                sender().send(SendMessageVo.create(messageVo, CommonResponse.toHome(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
        }
    }

    private void handleProductCommand(String productCode, MessageVo messageVo, List<String> productCommands) {
        final String telegramId = messageVo.getTelegramId();

        sender().send(SendMessageVo.create(messageVo, CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final CrawledProduct crawledProduct = danawaProductCache.get(productCode);
        if (Objects.isNull(crawledProduct)) {
            sender().send(SendMessageVo.create(messageVo, ProductSearchAddResponse.failPoolProduct(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        sender().send(SendMessageVo.create(messageVo, CommonResponse.descCrawledProductVo(crawledProduct), crawledProduct.getImageUrl(), Keyboard.getProductSearchAddKeyboard(productCode), callbackFactory.create(telegramId, Menu.ITEM_SEARCH_ADD)));
        sender().send(SendMessageVo.create(messageVo, ProductSearchAddResponse.explain(), null, null, callbackFactory.createDefaultNoAction(telegramId)));
    }

}
