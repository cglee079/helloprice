package com.podo.helloprice.telegram.app.menu.home;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.emailadd.EmailAddResponse;
import com.podo.helloprice.telegram.app.menu.emaildelete.EmailDeleteResponse;
import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import com.podo.helloprice.telegram.app.menu.global.ProductCommandTranslator;
import com.podo.helloprice.telegram.app.menu.productadd.ProductAddResponse;
import com.podo.helloprice.telegram.app.menu.productdelete.ProductDeleteResponse;
import com.podo.helloprice.telegram.app.menu.productsearch.ProductSearchResponse;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private final ProductReadService productReadService;

    private final UserProductNotifyService userProductNotifyService;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.HOME;
    }

    public void handle(MessageVo messageVo, String requestMessage) {
        final String telegramId = messageVo.getTelegramId();
        final List<String> productCommands = ProductCommandTranslator.getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        log.debug("APP :: {} << 홈메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final HomeCommand requestHomeCommand = HomeCommand.from(requestMessage);

        if (Objects.nonNull(requestHomeCommand)) {
            handleCommand(messageVo, requestMessage, telegramId, requestHomeCommand, productCommands);
            return;
        }

        final String productCode = ProductCommandTranslator.getProductCodeFromCommand(requestMessage);
        if (Objects.isNull(productCode)) {
            log.debug("APP :: {} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        if (!productReadService.isExistedByProductCode(productCode)) {
            log.debug("APP :: {} << 잘못된 상품코드 메세지입니다. 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(SendMessageVo.create(messageVo, HomeResponse.wrongProductCode(productCode), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        log.debug("APP :: {} << 상품정보 요청을 확인했습니다. 받은메세지 '{}'", telegramId, requestMessage);
        handleProductCommands(messageVo, telegramId, productCode);
    }

    private void handleProductCommands(MessageVo messageVo, String telegramId, String productCode) {
        final ProductDetailDto product = productReadService.findByProductCode(productCode);
        sender().send(SendMessageVo.create(messageVo, HomeResponse.descProduct(product), product.getImageUrl(), null, callbackFactory.create(telegramId, Menu.HOME)));
    }

    private void handleCommand(MessageVo messageVo, String requestMessage, String telegramId, HomeCommand requestCommand, List<String> productCommands) {
        switch (requestCommand) {
            case ITEM_SEARCH_ADD:
                log.debug("APP :: {} << 상품 검색 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(SendMessageVo.create(messageVo, ProductSearchResponse.explain(), Keyboard.getDefaultKeyboard(), callbackFactory.create(telegramId, Menu.ITEM_SEARCH)));
                break;

            case ITEM_ADD:
                log.debug("APP :: {} << 상품 추가 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(SendMessageVo.create(messageVo, ProductAddResponse.explain(DANAWA_URL, helpUrl), Keyboard.getDefaultKeyboard(), callbackFactory.create(telegramId, Menu.ITEM_ADD)));
                break;

            case ITEM_DELETE:
                log.debug("APP :: {} << 상품 삭제 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(SendMessageVo.create(messageVo, ProductDeleteResponse.explain(), Keyboard.getProductDeleteKeyboard(productCommands), callbackFactory.create(telegramId, Menu.ITEM_DELETE)));
                break;

            case EMAIL_ADD:
                handleEmailAddCommand(messageVo, productCommands);
                break;

            case EMAIL_DELETE:
                handleEmailDeleteCommand(messageVo, productCommands);
                break;

            case HELP:
                log.debug("APP :: {} << 도움말. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(SendMessageVo.create(messageVo, CommonResponse.introduce(appDesc), null, callbackFactory.create(telegramId, Menu.HOME)));
                sender().send(SendMessageVo.create(messageVo, CommonResponse.help(helpUrl), null, callbackFactory.create(telegramId, null)));
                break;
        }
    }

    private void handleEmailAddCommand(MessageVo messageVo, List<String> productCommands) {
        final String telegramId = messageVo.getTelegramId();

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final String userEmail = user.getEmail();

        if (Objects.nonNull(userEmail)) {
            log.debug("APP :: {} << 이미 이메일이 등록되어있습니다. 기존이메일 '{}'", telegramId, userEmail);
            sender().send(SendMessageVo.create(messageVo, HomeResponse.rejectEmailAdd(userEmail), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        log.debug("APP :: {} << 이메일 추가 메뉴로 이동", telegramId);
        sender().send(SendMessageVo.create(messageVo, EmailAddResponse.explain(), Keyboard.getDefaultKeyboard(), callbackFactory.create(telegramId, Menu.EMAIL_ADD)));
    }

    private void handleEmailDeleteCommand(MessageVo messageVo, List<String> productCommands) {
        final String telegramId = messageVo.getTelegramId();

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);
        final String userEmail = user.getEmail();

        if (Objects.isNull(userEmail)) {
            log.debug("APP :: {} << 등록되어있는 이메일이 없습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, HomeResponse.dontHaveEmail(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        log.debug("APP :: {} << 이메일 삭제 메뉴로 이동", telegramId);
        sender().send(SendMessageVo.create(messageVo, EmailDeleteResponse.explain(userEmail), Keyboard.getEmailDeleteKeyboard(), callbackFactory.create(telegramId, Menu.EMAIL_DELETE)));
    }
}
