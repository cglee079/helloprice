package com.podo.helloprice.telegram.app.menu.productdelete;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import com.podo.helloprice.telegram.app.menu.global.ProductCommandTranslator;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductDeleteMenuHandler extends AbstractMenuHandler {

    private final UserReadService userReadService;
    private final ProductReadService productReadService;

    private final UserProductNotifyService userProductNotifyService;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_DELETE;
    }

    public void handle(MessageVo messageVo, String requestMessage) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("APP :: {} << 상품 알림 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> productCommands = ProductCommandTranslator.getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        final ProductDeleteCommand requestProductDeleteCommand = ProductDeleteCommand.from(requestMessage);
        if (Objects.nonNull(requestProductDeleteCommand)) {
            handleCommand(requestProductDeleteCommand, messageVo, productCommands);
            return;
        }

        final String productCodeFromRequestCommand = ProductCommandTranslator.getProductCodeFromCommand(requestMessage);

        if (Objects.isNull(productCodeFromRequestCommand)) {
            log.debug("APP :: {} << 잘못된 값을 입력했습니다. 상품코드를 찾을 수 없습니다. 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductDetailDto product = productReadService.findByProductCode(productCodeFromRequestCommand);

        if (!userProductNotifyService.isExistedNotify(user.getId(), product.getId())) {
            log.debug("APP :: {} << 삭제 요청한 {}({}) 상품 알림이 등록되어있지 않습니다. 받은메세지 '{}'", telegramId, product.getProductName(), productCodeFromRequestCommand, requestMessage);
            sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.alreadyNotNotifyProduct(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
        }

        userProductNotifyService.deleteNotifyByUserIdAndProductId(user.getId(), product.getId());
        final List<String> reloadProductCommands = ProductCommandTranslator.getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));
        sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.deletedNotifyProduct(product), Keyboard.getHomeKeyboard(reloadProductCommands), callbackFactory.create(telegramId, Menu.HOME)));

    }

    private void handleCommand(ProductDeleteCommand productDeleteCommand, MessageVo messageVo, List<String> productCommands) {
        switch (productDeleteCommand) {
            case EXIT:
                sender().send(SendMessageVo.create(messageVo, CommonResponse.toHome(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
        }
    }
}

