package com.podo.helloprice.telegram.app.menu.email.delete;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.user.application.UserWriteService;
import com.podo.helloprice.telegram.domain.user.model.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailDeleteMenuHandler extends AbstractMenuHandler {

    private final UserWriteService userWriteService;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.EMAIL_DELETE;
    }

    public void handle(MessageVo messageVo, String requestMessage) {
        final String telegramId = messageVo.getTelegramId();
        final HomeKeyboard homeKeyboard = createHomeKeyboard(telegramId);

        log.debug("TELEGRAM :: {} << 이메일 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final EmailDeleteCommand requestCommand = EmailDeleteCommand.from(requestMessage);

        if (Objects.isNull(requestCommand)) {
            sender().send(SendMessageVo.create(messageVo, CommonResponse.wrongInput(), homeKeyboard, callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        handleEmailDeleteCommand(requestCommand, messageVo, homeKeyboard);
    }

    private void handleEmailDeleteCommand(EmailDeleteCommand requestCommand, MessageVo messageVo, HomeKeyboard homeKeyboard) {
        switch (requestCommand) {
            case YES:
                userWriteService.updateEmailByTelegramId(messageVo.getTelegramId(), null);
                sender().send(SendMessageVo.create(messageVo, EmailDeleteResponse.success(), homeKeyboard, callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
                break;
            case NO:
                sender().send(SendMessageVo.create(messageVo, CommonResponse.toHome(), homeKeyboard, callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
        }
    }
}
