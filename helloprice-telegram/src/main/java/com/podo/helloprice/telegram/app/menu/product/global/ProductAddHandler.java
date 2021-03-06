package com.podo.helloprice.telegram.app.menu.product.global;


import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.menu.product.typeselect.ProductTypeCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.typeselect.ProductTypeSelectKeyboard;
import com.podo.helloprice.telegram.app.value.MessageVo;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.value.SendMessageVo;
import com.podo.helloprice.telegram.domain.tusernotify.application.TUserNotifyReadService;
import com.podo.helloprice.telegram.global.cache.DanawaProductCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductAddHandler {

    private final TUserNotifyReadService tUserNotifyReadService;
    private final TelegramMessageSender sender;
    private final DanawaProductCache danawaProductCache;
    private final SendMessageCallbackFactory callbackFactory;

    public void handleProductAdd(MessageVo messageVo, String productCode) {
        final String telegramId = messageVo.getTelegramId();
        final List<String> productDescCommands = ProductDescCommandTranslator.encodes(tUserNotifyReadService.findByTelegramId(telegramId));
        final HomeKeyboard homeKeyboard = new HomeKeyboard(productDescCommands);

        final CrawledProduct crawledProduct = danawaProductCache.get(productCode);
        if (CrawledProduct.FAIL.equals(crawledProduct)) {
            log.debug("APP :: {} << 상품 정보를 가져 올 수 없습니다. 상품코드 '{}'", telegramId, productCode);
            sender.send(SendMessageVo.create(messageVo, ProductAddResponse.wrongProductCode(productCode), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        if (!validateAddProduct(messageVo, crawledProduct, homeKeyboard)) {
            return;
        }

        sender.send(
                SendMessageVo.create(messageVo, ProductAddResponse.descCrawledProduct(crawledProduct),
                        crawledProduct.getImageUrl(),
                        new ProductTypeSelectKeyboard(ProductTypeCommandTranslator.encode(crawledProduct)),
                        callbackFactory.create(telegramId, Menu.PRODUCT_TYPE_SELECT))
        );
    }

    private boolean validateAddProduct(MessageVo messageVo, CrawledProduct crawledProduct, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();
        final String productName = crawledProduct.getProductName();
        final String productCode = crawledProduct.getProductCode();

        switch (crawledProduct.getSaleStatus()) {
            case DISCONTINUE:
                log.debug("APP :: {} << 추가요청한 {}({})는 단종된 상품 입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isDiscontinueProduct(crawledProduct), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
                return false;
            case NOT_SUPPORT:
                log.debug("APP :: {} << 추가요청한 {}({})는 가격비교중지 상품입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isNotSupportProduct(crawledProduct), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
                return false;
            case UNKNOWN:
                log.debug("APP :: {} << 추가요청한 상품 {}({})는 알 수 없는 상태의 상품입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isErrorProduct(crawledProduct), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
                return false;
        }

        return true;
    }
}
