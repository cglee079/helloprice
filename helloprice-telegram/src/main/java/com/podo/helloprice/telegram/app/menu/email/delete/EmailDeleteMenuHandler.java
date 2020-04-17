package com.podo.helloprice.telegram.app.menu.email.delete;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.KeyboardHelper;
import com.podo.helloprice.telegram.app.menu.product.ProductDescCommandTranslator;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.user.application.UserWriteService;
import com.podo.helloprice.telegram.domain.user.model.Menu;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailDeleteMenuHandler extends AbstractMenuHandler {

    private final UserWriteService userWriteService;
    private final UserProductNotifyService userProductNotifyService;

    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.EMAIL_DELETE;
    }

    public void handle(MessageVo messageVo, String requestMessage) {

        final String telegramId = messageVo.getTelegramId();

        log.debug("TELEGRAM :: {} << 이메일 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> productCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        final EmailDeleteCommand requestCommand = EmailDeleteCommand.from(requestMessage);
        if (Objects.isNull(requestCommand)) {
            sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.wrongInput(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        handleEmailDeleteCommand(requestCommand, messageVo, productCommands);
    }

    private void handleEmailDeleteCommand(EmailDeleteCommand requestCommand, MessageVo messageVo, List<String> productCommands) {
        switch (requestCommand) {
            case YES:
                handleYesCommand(messageVo, productCommands);
                break;
            case NO:
                sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.toHome(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
        }
    }

    private void handleYesCommand(MessageVo messageVo, List<String> productCommands) {
        final String telegramId = messageVo.getTelegramId();

        userWriteService.updateEmailByTelegramId(telegramId, null);

        sender().send(SendMessageVo.create(messageVo, EmailDeleteResponse.success(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
    }
}
