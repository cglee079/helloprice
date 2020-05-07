package com.podo.helloprice.telegram.app.menu.home;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.email.add.EmailAddResponse;
import com.podo.helloprice.telegram.app.menu.email.delete.EmailDeleteKeyboard;
import com.podo.helloprice.telegram.app.menu.email.delete.EmailDeleteResponse;
import com.podo.helloprice.telegram.app.menu.product.addurl.ProductAddUrlResponse;
import com.podo.helloprice.telegram.app.menu.product.delete.ProductDeleteCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.delete.ProductDeleteKeyboard;
import com.podo.helloprice.telegram.app.menu.product.delete.ProductDeleteResponse;
import com.podo.helloprice.telegram.app.menu.product.global.ProductDescCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.global.ProductDescParameter;
import com.podo.helloprice.telegram.app.menu.product.search.ProductSearchResponse;
import com.podo.helloprice.telegram.app.value.MessageVo;
import com.podo.helloprice.telegram.app.value.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.dto.ProductDto;
import com.podo.helloprice.telegram.domain.productsale.application.ProductSaleReadService;
import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.userproduct.application.UserProductSaleNotifyReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class HomeMenuHandler extends AbstractMenuHandler {

    private static final String DANAWA_URL = "http://www.danawa.com";

    @Value("${app.name} ${app.version}")
    private String appDesc;

    @Value("${app.help_url}")
    private String helpUrl;

    private final UserReadService userReadService;
    private final ProductSaleReadService productSaleReadService;
    private final ProductReadService productReadService;

    private final UserProductSaleNotifyReadService userProductSaleNotifyReadService;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.HOME;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();
        final HomeKeyboard homeKeyboard = createHomeKeyboard(telegramId);

        log.debug("APP :: {} << 홈메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        final HomeCommand homeCommand = HomeCommand.from(messageContents);

        if (Objects.nonNull(homeCommand)) {
            handleCommand(messageVo, messageContents, telegramId, homeCommand, homeKeyboard);
            return;
        }

        final ProductDescParameter productDescParameter = ProductDescCommandTranslator.decode(messageContents);

        if (Objects.isNull(productDescParameter)) {
            log.debug("APP :: {} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, messageContents);
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        if (!productReadService.isExistedByProductParameter(productDescParameter.getProductCode())) {
            log.debug("APP :: {} << 잘못된 상품코드 메세지입니다. 받은메세지 '{}'", telegramId, messageContents);
            sender().send(SendMessageVo.create(messageVo, HomeResponse.wrongProductCode(productDescParameter.getProductCode()), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        log.debug("APP :: {} << 상품정보 요청을 확인했습니다. 받은메세지 '{}'", telegramId, messageContents);
        handleProductDescCommands(messageVo, telegramId, productDescParameter);
    }

    private void handleProductDescCommands(MessageVo messageVo, String telegramId, ProductDescParameter productDescParameter) {
        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final ProductSaleDto productSale = productSaleReadService.findByProductCodeAndSaleType(productDescParameter.getProductCode(), productDescParameter.getSaleType());
        final ProductDto product = productSale.getProduct();

        //상품알림이 등록되어있지 않은 경우.
        if (!userProductSaleNotifyReadService.isExistedNotify(user.getId(), productSale.getId())) {
            log.debug("APP :: {} << 등록되지 않았거나, 알림 삭제된 상품입니다 : {}", telegramId, product.getProductCode());
            sender().send(SendMessageVo.create(messageVo, HomeResponse.invalidNotifyProduct(), createHomeKeyboard(telegramId), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        sender().send(SendMessageVo.create(messageVo, HomeResponse.descProductSale(productSale), product.getImageUrl(), null, callbackFactory.create(telegramId, Menu.HOME)));
    }

    private void handleCommand(MessageVo messageVo, String requestMessage, String telegramId, HomeCommand requestCommand, HomeKeyboard homeKeyboard) {
        switch (requestCommand) {
            case ITEM_SEARCH_ADD:
                log.debug("APP :: {} << 상품 검색 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(SendMessageVo.create(messageVo, ProductSearchResponse.explain(), Keyboard.TYPING_KEYBOARD, callbackFactory.create(telegramId, Menu.PRODUCT_SEARCH)));
                break;

            case ITEM_ADD:
                log.debug("APP :: {} << 상품 추가 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(SendMessageVo.create(messageVo, ProductAddUrlResponse.explain(DANAWA_URL, helpUrl), Keyboard.TYPING_KEYBOARD, callbackFactory.create(telegramId, Menu.PRODUCT_ADD)));
                break;

            case ITEM_DELETE:
                log.debug("APP :: {} << 상품 삭제 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                final List<String> productDeleteCommands = ProductDeleteCommandTranslator.encodes(userProductSaleNotifyReadService.findByTelegramId(telegramId));
                sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.explain(), new ProductDeleteKeyboard(productDeleteCommands), callbackFactory.create(telegramId, Menu.PRODUCT_DELETE)));
                break;

            case EMAIL_ADD:
                handleEmailAddCommand(messageVo, homeKeyboard);
                break;

            case EMAIL_DELETE:
                handleEmailDeleteCommand(messageVo, homeKeyboard);
                break;

            case HELP:
                log.debug("APP :: {} << 도움말. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(SendMessageVo.create(messageVo, CommonResponse.introduce(appDesc), null, callbackFactory.create(telegramId, Menu.HOME)));
                sender().send(SendMessageVo.create(messageVo, CommonResponse.help(helpUrl), null, callbackFactory.create(telegramId, null)));
                break;
        }
    }

    private void handleEmailAddCommand(MessageVo messageVo, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final String userEmail = user.getEmail();

        if (Objects.nonNull(userEmail)) {
            log.debug("APP :: {} << 이미 이메일이 등록되어있습니다. 기존이메일 '{}'", telegramId, userEmail);
            sender().send(SendMessageVo.create(messageVo, HomeResponse.rejectEmailAdd(userEmail), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        log.debug("APP :: {} << 이메일 추가 메뉴로 이동", telegramId);
        sender().send(SendMessageVo.create(messageVo, EmailAddResponse.explain(), Keyboard.TYPING_KEYBOARD, callbackFactory.create(telegramId, Menu.EMAIL_ADD)));
    }

    private void handleEmailDeleteCommand(MessageVo messageVo, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final String userEmail = user.getEmail();

        if (StringUtils.isEmpty(userEmail)) {
            log.debug("APP :: {} << 등록되어있는 이메일이 없습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, HomeResponse.dontHaveEmail(), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        log.debug("APP :: {} << 이메일 삭제 메뉴로 이동", telegramId);
        sender().send(SendMessageVo.create(messageVo, EmailDeleteResponse.explain(userEmail), new EmailDeleteKeyboard(), callbackFactory.create(telegramId, Menu.EMAIL_DELETE)));
    }
}
