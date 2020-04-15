package com.podo.helloprice.telegram.app.menu.product.typeselect;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.KeyboardHelper;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import com.podo.helloprice.telegram.app.menu.product.ProductAddResponse;
import com.podo.helloprice.telegram.app.menu.product.ProductDescCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.ProductTypeCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.ProductTypeParameter;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.application.ProductWriteService;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.product.model.PriceType;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import com.podo.helloprice.telegram.global.cache.DanawaProductCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductTypeSelectHandler extends AbstractMenuHandler {

    private final ProductReadService productReadService;
    private final ProductWriteService productWriteService;
    private final UserReadService userReadService;
    private final UserProductNotifyService userProductNotifyService;
    private final SendMessageCallbackFactory callbackFactory;
    private final DanawaProductCache danawaProductCache;

    public Menu getHandleMenu() {
        return Menu.PRODUCT_TYPE_SELECT;
    }

    @Override
    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();

        final ProductTypeParameter productTypeParameter = ProductTypeCommandTranslator.decode(messageContents);
        final String productCode = productTypeParameter.getProductCode();
        final PriceType priceType = productTypeParameter.getPriceType();

        final Long productId = productWriteService.writeCrawledProduct(danawaProductCache.get(productCode));

        final List<String> productDescCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));
        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductDetailDto product = productReadService.findByProductId(productId, priceType);

        if (userProductNotifyService.isExistedNotify(user.getId(), product.getId(), product.getPriceType())) {
            log.debug("APP :: {} << {}({}) 상품 알림이 이미 등록되었습니다.", telegramId, product.getProductName(), productCode);
            sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.descProductDetail(product), product.getImageUrl(), KeyboardHelper.getHomeKeyboard(productDescCommands), callbackFactory.create(telegramId, Menu.HOME)));
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.alreadySetNotifyProduct(), null, null, callbackFactory.create(telegramId, null)));
            return;
        }

        if (userReadService.hasMaxNotifyByUserTelegramId(telegramId)) {
            log.debug("APP :: {} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, ProductAddResponse.hasMaxProduct(), null, KeyboardHelper.getHomeKeyboard(productDescCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        userProductNotifyService.addNewNotify(user.getId(), product.getId() , productTypeParameter.getPriceType());

        final List<String> refreshProductDescCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));
        sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.descProductDetail(product), product.getImageUrl(), KeyboardHelper.getHomeKeyboard(refreshProductDescCommands), callbackFactory.create(telegramId, Menu.HOME)));
        sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.successAddNotifyProduct(), null, null, callbackFactory.create(telegramId, null)));
    }

}
