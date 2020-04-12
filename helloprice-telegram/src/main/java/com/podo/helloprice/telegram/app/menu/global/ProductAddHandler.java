package com.podo.helloprice.telegram.app.menu.global;


import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.core.TelegramMessageSender;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.productadd.ProductAddResponse;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.application.ProductWriteService;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.application.UserWriteService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import com.podo.helloprice.telegram.global.cache.DanawaProductCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.podo.helloprice.telegram.app.menu.global.ProductCommandTranslator.getProductCommands;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductAddHandler {

    private final ProductReadService productReadService;
    private final ProductWriteService productWriteService;

    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    private final UserProductNotifyService userProductNotifyService;

    private final TelegramMessageSender sender;
    private final DanawaProductCache danawaProductCache;

    private final SendMessageCallbackFactory callbackFactory;

    public void handleProductAdd(MessageVo messageVo, String productCode) {
        final String telegramId = messageVo.getTelegramId();
        final List<String> productCommands = getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        final CrawledProduct crawledProduct = danawaProductCache.get(productCode);
        if (Objects.isNull(crawledProduct)) {
            log.debug("APP :: {} << 상품 정보를 가져 올 수 없습니다. 상품코드 '{}'", telegramId, productCode);

            sender.send(SendMessageVo.create(messageVo, ProductAddResponse.wrongProductCode(productCode), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        if (!validateNewProduct(messageVo, productCommands, crawledProduct)) {
            return;
        }

        final Long productId = productWriteService.writeCrawledProduct(crawledProduct);

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductDetailDto product = productReadService.findByProductId(productId);

        if (userProductNotifyService.isExistedNotify(user.getId(), product.getId())) {
            log.debug("APP :: {} << {}({}) 상품 알림이 이미 등록되었습니다.", telegramId, crawledProduct.getProductName(), productCode);
            sender.send(SendMessageVo.create(messageVo, CommonResponse.descProductDetail(product), product.getImageUrl(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            sender.send(SendMessageVo.create(messageVo, ProductAddResponse.alreadySetNotifyProduct(), null, null, callbackFactory.create(telegramId, null)));
            return;
        }

        log.debug("APP :: {} << {}({}) 상품 알림을 등록합니다.", telegramId, crawledProduct.getProductName(), productCode);

        if (userReadService.hasMaxNotifyByUserTelegramId(telegramId)) {
            log.debug("APP :: {} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender.send(SendMessageVo.create(messageVo, ProductAddResponse.hasMaxProduct(), null, Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        handleProductCommand(messageVo, telegramId, crawledProduct, productId, user, product);
    }

    private void handleProductCommand(MessageVo messageVo, String telegramId, CrawledProduct crawledProduct, Long productId, UserDetailDto userDetail, ProductDetailDto productDetail) {
        userProductNotifyService.addNewNotify(userDetail.getId(), productId);

        final List<String> reloadProductCommands = getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId)); // 갱신
        sender.send(SendMessageVo.create(messageVo, CommonResponse.descProductDetail(productDetail), crawledProduct.getImageUrl(), Keyboard.getHomeKeyboard(reloadProductCommands), callbackFactory.create(telegramId, Menu.HOME)));
        sender.send(SendMessageVo.create(messageVo, ProductAddResponse.successAddNotifyProduct(), null, null, callbackFactory.create(telegramId, null)));
    }


    private boolean validateNewProduct(MessageVo messageVo, List<String> productCommands, CrawledProduct crawledProduct) {
        final String telegramId = messageVo.getTelegramId();
        final String productName = crawledProduct.getProductName();
        final String productCode = crawledProduct.getProductCode();

        switch (crawledProduct.getSaleStatus()) {
            case DISCONTINUE:
                log.debug("APP :: {} << 추가요청한 {}({})는 단종된 상품 입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isDiscontinueProduct(crawledProduct), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
                return false;
            case NOT_SUPPORT:
                log.debug("APP :: {} << 추가요청한 {}({})는 가격비교중지 상품입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isNotSupportProduct(crawledProduct), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
                return false;
            case UNKNOWN:
                log.debug("APP :: {} << 추가요청한 상품 {}({})는 알 수 없는 상태의 상품입니다", telegramId, productName, productCode);
                sender.send(SendMessageVo.create(messageVo, ProductAddResponse.isErrorProduct(crawledProduct), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
                return false;
        }

        return true;
    }
}
