package com.podo.helloprice.telegram.app.menu.product.delete;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.dto.ProductOnePriceTypeDto;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.userproduct.application.UserProductNotifyReadService;
import com.podo.helloprice.telegram.domain.userproduct.application.UserProductNotifyWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.podo.helloprice.telegram.app.menu.product.delete.ProductDeleteCommand.EXIT;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductDeleteMenuHandler extends AbstractMenuHandler {

    private final UserReadService userReadService;
    private final ProductReadService productReadService;

    private final UserProductNotifyReadService userProductNotifyReadService;
    private final UserProductNotifyWriteService userProductNotifyWriteService;

    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.PRODUCT_DELETE;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();
        final HomeKeyboard homeKeyboard = createHomeKeyboard(telegramId);

        log.debug("APP :: {} << 상품 알림 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);


        final ProductDeleteCommand productDeleteCommand = ProductDeleteCommand.from(messageContents);

        //`EXIT` Command
        if (Objects.nonNull(productDeleteCommand) && productDeleteCommand.equals(EXIT)) {
            sender().send(SendMessageVo.create(messageVo, CommonResponse.toHome(), homeKeyboard, callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        final ProductDeleteParameter productDeleteParameter = ProductDeleteCommandTranslator.decode(messageContents);

        if (Objects.isNull(productDeleteParameter)) {
            log.debug("APP :: {} << 잘못된 값을 입력했습니다. 상품코드를 찾을 수 없습니다. 받은메세지 '{}'", telegramId, messageContents);
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductOnePriceTypeDto product = productReadService.findByProductParameter(productDeleteParameter.getProductCode(), productDeleteParameter.getPriceType());

        if (!userProductNotifyReadService.isExistedNotify(user.getId(), product.getId(), product.getPriceType())) {
            log.debug("APP :: {} << 삭제 요청한 {}({}) 상품 알림이 등록되어있지 않습니다. 받은메세지 '{}'", telegramId, product.getProductName(), productDeleteParameter, messageContents);
            sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.alreadyNotNotifyProduct(), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
        }

        userProductNotifyWriteService.deleteNotifyByUserIdAndProductId(user.getId(), product.getId(), productDeleteParameter.getPriceType());

        sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.deletedNotifyProduct(product), createHomeKeyboard(telegramId), callbackFactory.create(telegramId, Menu.HOME)));
    }
}

