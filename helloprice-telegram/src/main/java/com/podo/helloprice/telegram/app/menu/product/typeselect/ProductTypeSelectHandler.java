package com.podo.helloprice.telegram.app.menu.product.typeselect;


import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.value.MessageVo;
import com.podo.helloprice.telegram.app.value.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.application.ProductWriteService;
import com.podo.helloprice.telegram.domain.product.dto.ProductDto;
import com.podo.helloprice.telegram.domain.productsale.application.ProductSaleReadService;
import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.telegram.domain.tuser.application.TUserReadService;
import com.podo.helloprice.telegram.domain.tuser.dto.TUserDetailDto;
import com.podo.helloprice.telegram.domain.tusernotify.application.TUserNotifyReadService;
import com.podo.helloprice.telegram.domain.tusernotify.application.TUerNotifyWriteService;
import com.podo.helloprice.telegram.global.cache.DanawaProductCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.podo.helloprice.telegram.app.menu.Menu.HOME;
import static com.podo.helloprice.telegram.app.menu.product.typeselect.ProductTypeSelectCommand.EXIT;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductTypeSelectHandler extends AbstractMenuHandler {

    private final TUserReadService userReadService;

    private final ProductReadService productReadService;
    private final ProductSaleReadService productSaleReadService;
    private final ProductWriteService productWriteService;

    private final TUserNotifyReadService tUserNotifyReadService;
    private final TUerNotifyWriteService tUerNotifyWriteService;

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
        final List<SaleType> saleTypes = productTypeParameter.getSaleTypes();

        final Long productId = productWriteService.writeCrawledProduct(danawaProductCache.get(productCode));

        if (saleTypes.size() > 1) {
            handleAllPriceType(messageVo, productId, homeKeyboard);
            return;
        }

         handleOnePriceType(messageVo, productId, saleTypes.get(0), homeKeyboard);
    }

    private void handleAllPriceType(MessageVo messageVo, Long productId, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();
        final TUserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductDto product = productReadService.findByProductId(productId);
        final Map<SaleType, ProductSaleDto> saleTypeToProductSale = productSaleReadService.findByProductId(productId);

        final Long userId = user.getId();
        final String productName = product.getProductName();
        final Set<SaleType> saleTypes = saleTypeToProductSale.keySet();

        for (SaleType saleType : saleTypes) {
            if (tUserNotifyReadService.isExistedNotify(userId, saleTypeToProductSale.get(saleType).getId())) {
                log.debug("APP :: {} << {} 상품 알림이 이미 등록되었습니다.", telegramId, productName);
                sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.alreadySetNotifyProduct(productName, saleType), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
                return;
            }
        }

        if (userReadService.hasMaxNotifyByTelegramIdIfAdded(telegramId, saleTypes.size())){
            log.debug("APP :: {} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.hasMaxProduct(), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        for (SaleType saleType : saleTypes) {
            tUerNotifyWriteService.insertNewNotify(userId, saleTypeToProductSale.get(saleType).getId());
        }

        sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.successAddNotifyProduct(), product.getImageUrl(),  createHomeKeyboard(telegramId), callbackFactory.create(telegramId, HOME)));
    }

    private void handleOnePriceType(MessageVo messageVo, Long productId, SaleType saleType, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();

        final TUserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductSaleDto productSale = productSaleReadService.findByProductIdAndSaleType(productId, saleType);
        final ProductDto product = productSale.getProduct();

        final Long userId = user.getId();
        final String productName = product.getProductName();

        if (tUserNotifyReadService.isExistedNotify(userId, productSale.getId())) {
            log.debug("APP :: {} << {} 상품 알림이 이미 등록되었습니다.", telegramId, productName);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.alreadySetNotifyProduct(productName, saleType), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        if (userReadService.hasMaxNotifyByTelegramId(telegramId)) {
            log.debug("APP :: {} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.hasMaxProduct(), null, homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        tUerNotifyWriteService.insertNewNotify(userId, productSale.getId());

        sender().send(SendMessageVo.create(messageVo, ProductTypeSelectResponse.successAddNotifyProduct(), product.getImageUrl(),  createHomeKeyboard(telegramId), callbackFactory.create(telegramId, HOME)));
    }

}
