package com.podo.helloprice.telegram.app.menu.product;


import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.menu.KeyboardHelper;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.domain.user.model.Menu;
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
public class ProductAddHandler {

    private final UserProductNotifyService userProductNotifyService;
    private final TelegramMessageSender sender;
    private final DanawaProductCache danawaProductCache;
    private final SendMessageCallbackFactory callbackFactory;

    public void handleProductAdd(MessageVo messageVo, String productCode) {
        final String telegramId = messageVo.getTelegramId();
        final List<String> productDescCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        final CrawledProduct crawledProduct = danawaProductCache.get(productCode);
        if (Objects.isNull(crawledProduct)) {
            log.debug("APP :: {} << 상품 정보를 가져 올 수 없습니다. 상품코드 '{}'", telegramId, productCode);
            sender.send(SendMessageVo.create(messageVo, ProductAddResponse.wrongProductCode(productCode), KeyboardHelper.getHomeKeyboard(productDescCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        if (!validateAddProduct(messageVo, productDescCommands, crawledProduct)) {
            return;
        }

        sender.send(
                SendMessageVo.create(messageVo, ProductAddResponse.descCrawledProduct(crawledProduct),
                        crawledProduct.getImageUrl(),
                        KeyboardHelper.getProductTypeSelectKeyboard(ProductTypeCommandTranslator.encode(crawledProduct)),
                        callbackFactory.create(telegramId, Menu.PRODUCT_TYPE_SELECT))
        );
    }

    private boolean validateAddProduct(MessageVo messageVo, List<String> productCommands, CrawledProduct crawledProduct) {
        final String telegramId = messageVo.getTelegramId();
        final String productName = crawledProduct.getProductName();
        final String productCode = crawledProduct.getProductCode();

        switch (crawledProduct.getSaleStatus()) {
            case DISCONTINUE:
                log.debug("APP :: {} << 추가요청한 {}({})는 단종된 상품 입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isDiscontinueProduct(crawledProduct), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
                return false;
            case NOT_SUPPORT:
                log.debug("APP :: {} << 추가요청한 {}({})는 가격비교중지 상품입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isNotSupportProduct(crawledProduct), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
                return false;
            case UNKNOWN:
                log.debug("APP :: {} << 추가요청한 상품 {}({})는 알 수 없는 상태의 상품입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isErrorProduct(crawledProduct), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
                return false;
        }

        return true;
    }
}
