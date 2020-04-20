package com.podo.helloprice.telegram.app.menu.product.typeselect;


import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.application.ProductWriteService;
import com.podo.helloprice.telegram.domain.product.dto.ProductOneMorePriceTypeDto;
import com.podo.helloprice.telegram.domain.product.dto.ProductOnePriceTypeDto;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.user.model.Menu;
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
import static com.podo.helloprice.telegram.domain.user.model.Menu.HOME;

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
        final HomeKeyboard homeKeyboard = getHomeKeyboard(telegramId);

        final ProductTypeSelectCommand command = ProductTypeSelectCommand.from(messageContents);

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
            handleOneMorePriceType(messageVo, productId, homeKeyboard);
            return;
        }

         handleOnePriceType(messageVo, productId, priceTypes.get(0), homeKeyboard);
    }

    private void handleOneMorePriceType(MessageVo messageVo, Long productId, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();
        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductOneMorePriceTypeDto product = productReadService.findByProductId(productId);

        final String productName = product.getProductName();
        final Set<PriceType> priceTypes = product.getPrices().keySet();

        for (PriceType priceType : priceTypes) {
            if (userProductNotifyReadService.isExistedNotify(user.getId(), product.getId(), priceType)) {
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
            userProductNotifyWriteService.insertNewNotify(user.getId(), product.getId(), priceType);
        }

        sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.successAddNotifyProduct(), product.getImageUrl(),  getHomeKeyboard(telegramId), callbackFactory.create(telegramId, HOME)));
    }

    private void handleOnePriceType(MessageVo messageVo, Long productId, PriceType priceType, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();
        final ProductOnePriceTypeDto product = productReadService.findByProductId(productId, priceType);
        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final String productName = product.getProductName();

        if (userProductNotifyReadService.isExistedNotify(user.getId(), product.getId(), product.getPriceType())) {
            log.debug("APP :: {} << {} 상품 알림이 이미 등록되었습니다.", telegramId, productName);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.alreadySetNotifyProduct(productName, priceType), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        if (userReadService.hasMaxNotifyByTelegramId(telegramId)) {
            log.debug("APP :: {} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.hasMaxProduct(), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        userProductNotifyWriteService.insertNewNotify(user.getId(), product.getId(), priceType);

        sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.successAddNotifyProduct(), product.getImageUrl(),  getHomeKeyboard(telegramId), callbackFactory.create(telegramId, HOME)));
    }

}
