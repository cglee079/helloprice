package com.podo.helloprice.telegram.app.menu.product.searchselect;

import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.product.global.ProductAddHandler;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.user.model.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.podo.helloprice.telegram.app.menu.product.searchselect.ProductSearchSelectCommand.EXIT;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductSearchSelectMenuHandler extends AbstractMenuHandler {

    private final ProductAddHandler productAddHandler;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.PRODUCT_SEARCH_RESULT;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("APP :: {} << 상품 검색 결과 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        final ProductSearchSelectCommand requestCommand = ProductSearchSelectCommand.from(messageContents);
        if (Objects.nonNull(requestCommand) && requestCommand.equals(EXIT)) {
            sender().send(SendMessageVo.create(messageVo, CommonResponse.toHome(), createHomeKeyboard(telegramId), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        final String productCode = ProductSearchSelectCommandTranslator.decode(messageContents);

        if (Objects.isNull(productCode)) {
            log.debug("APP :: {} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, messageContents);
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), createHomeKeyboard(telegramId), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        handleProductCommand(messageVo, productCode);
    }

    private void handleProductCommand(MessageVo messageVo, String productCode) {
        final String telegramId = messageVo.getTelegramId();

        sender().send(SendMessageVo.create(messageVo, CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        productAddHandler.handleProductAdd(messageVo, productCode);
    }

}
