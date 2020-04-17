package com.podo.helloprice.telegram.app.menu.product.delete;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.KeyboardHelper;
import com.podo.helloprice.telegram.domain.user.model.Menu;
import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import com.podo.helloprice.telegram.app.menu.product.ProductDescCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.ProductDescParameter;
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
        return Menu.PRODUCT_DELETE;
    }

    public void handle(MessageVo messageVo, String requestMessage) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("APP :: {} << 상품 알림 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> productCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        final ProductDeleteCommand requestProductDeleteCommand = ProductDeleteCommand.from(requestMessage);
        if (Objects.nonNull(requestProductDeleteCommand)) {
            handleCommand(requestProductDeleteCommand, messageVo, productCommands);
            return;
        }

        final ProductDescParameter productDescParameter = ProductDescCommandTranslator.decode(requestMessage);

        if (Objects.isNull(productDescParameter)) {
            log.debug("APP :: {} << 잘못된 값을 입력했습니다. 상품코드를 찾을 수 없습니다. 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.wrongInput(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductDetailDto product = productReadService.findByProductParameter(productDescParameter.getProductCode(), productDescParameter.getPriceType());

        if (!userProductNotifyService.isExistedNotify(user.getId(), product.getId(), product.getPriceType())) {
            log.debug("APP :: {} << 삭제 요청한 {}({}) 상품 알림이 등록되어있지 않습니다. 받은메세지 '{}'", telegramId, product.getProductName(), productDescParameter, requestMessage);
            sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.alreadyNotNotifyProduct(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
        }

        userProductNotifyService.deleteNotifyByUserIdAndProductId(user.getId(), product.getId(), productDescParameter.getPriceType());
        final List<String> reloadProductCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));
        sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.deletedNotifyProduct(product), KeyboardHelper.getHomeKeyboard(reloadProductCommands), callbackFactory.create(telegramId, Menu.HOME)));

    }

    private void handleCommand(ProductDeleteCommand productDeleteCommand, MessageVo messageVo, List<String> productCommands) {
        switch (productDeleteCommand) {
            case EXIT:
                sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.toHome(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
        }
    }
}

