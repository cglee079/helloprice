package com.podo.helloprice.telegram.app.menu.product.typeselect;


import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.value.MessageVo;
import com.podo.helloprice.telegram.app.value.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.application.ProductWriteService;
import com.podo.helloprice.telegram.domain.product.dto.ProductAllPriceTypeDto;
import com.podo.helloprice.telegram.domain.product.dto.ProductOnePriceTypeDto;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.userproduct.application.UserProductNotifyReadService;
import com.podo.helloprice.telegram.domain.userproduct.application.UserProductNotifyWriteService;
import com.podo.helloprice.telegram.global.cache.DanawaProductCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.podo.helloprice.telegram.app.menu.product.typeselect.ProductTypeSelectCommand.EXIT;
import static com.podo.helloprice.telegram.app.menu.Menu.HOME;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductTypeSelectHandler extends AbstractMenuHandler {

    private final UserReadService userReadService;

    private final ProductReadService productReadService;
    private final ProductWriteService productWriteService;

    private final UserProductNotifyReadService userProductNotifyReadService;
    private final UserProductNotifyWriteService userProductNotifyWriteService;

    private final SendMessageCallbackFactory callbackFactory;
    private final DanawaProductCache danawaProductCache;

    public Menu getHandleMenu() {
        return Menu.PRODUCT_TYPE_SELECT;
    }

    @Override
    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();
        final HomeKeyboard homeKeyboard = createHomeKeyboard(telegramId);

        final ProductTypeSelectCommand command = ProductTypeSelectCommand.from(messageContents);

        // EXIT Command
        if (Objects.nonNull(command) && command.equals(EXIT)) {
            sender().send(SendMessageVo.create(messageVo, CommonResponse.toHome(), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        final ProductTypeParameter productTypeParameter = ProductTypeCommandTranslator.decode(messageContents);

        if (Objects.isNull(productTypeParameter)) {
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        final String productCode = productTypeParameter.getProductCode();
        final List<PriceType> priceTypes = productTypeParameter.getPriceTypes();

        final Long productId = productWriteService.writeCrawledProduct(danawaProductCache.get(productCode));

        if (priceTypes.size() > 1) {
            handleAllPriceType(messageVo, productId, homeKeyboard);
            return;
        }

         handleOnePriceType(messageVo, productId, priceTypes.get(0), homeKeyboard);
    }

    private void handleAllPriceType(MessageVo messageVo, Long productId, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();
        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductAllPriceTypeDto product = productReadService.findByProductId(productId);

        final Long userId = user.getId();
        final String productName = product.getProductName();
        final Set<PriceType> priceTypes = product.getPrices().keySet();

        for (PriceType priceType : priceTypes) {
            if (userProductNotifyReadService.isExistedNotify(userId, productId, priceType)) {
                log.debug("APP :: {} << {} 상품 알림이 이미 등록되었습니다.", telegramId, productName);
                sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.alreadySetNotifyProduct(productName, priceType), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
                return;
            }
        }

        if (userReadService.hasMaxNotifyByTelegramIdIfAdded(telegramId, priceTypes.size())){
            log.debug("APP :: {} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.hasMaxProduct(), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        for (PriceType priceType : priceTypes) {
            userProductNotifyWriteService.insertNewNotify(userId, productId, priceType);
        }

        sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.successAddNotifyProduct(), product.getImageUrl(),  createHomeKeyboard(telegramId), callbackFactory.create(telegramId, HOME)));
    }

    private void handleOnePriceType(MessageVo messageVo, Long productId, PriceType priceType, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductOnePriceTypeDto product = productReadService.findByProductId(productId, priceType);

        final Long userId = user.getId();
        final String productName = product.getProductName();

        if (userProductNotifyReadService.isExistedNotify(userId, productId, product.getPriceType())) {
            log.debug("APP :: {} << {} 상품 알림이 이미 등록되었습니다.", telegramId, productName);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.alreadySetNotifyProduct(productName, priceType), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        if (userReadService.hasMaxNotifyByTelegramId(telegramId)) {
            log.debug("APP :: {} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.hasMaxProduct(), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        userProductNotifyWriteService.insertNewNotify(userId, productId, priceType);

        sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.successAddNotifyProduct(), product.getImageUrl(),  createHomeKeyboard(telegramId), callbackFactory.create(telegramId, HOME)));
    }

}
