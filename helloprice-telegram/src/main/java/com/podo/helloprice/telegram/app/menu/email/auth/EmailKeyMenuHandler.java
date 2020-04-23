package com.podo.helloprice.telegram.app.menu.email.auth;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.value.MessageVo;
import com.podo.helloprice.telegram.app.value.SendMessageVo;
import com.podo.helloprice.telegram.domain.user.application.UserWriteService;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.global.email.EmailAuthKeyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.podo.helloprice.telegram.app.menu.Menu.EMAIL_KEY;
import static com.podo.helloprice.telegram.app.menu.Menu.HOME;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailKeyMenuHandler extends AbstractMenuHandler {

    private final EmailAuthKeyStore emailAuthKeyStore;
    private final UserWriteService userWriteService;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return EMAIL_KEY;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();
        final String authKey = messageContents;
        final HomeKeyboard homeKeyboard = createHomeKeyboard(telegramId);

        log.debug("TELEGRAM :: {} << 이메일 인증 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        final String email = emailAuthKeyStore.getEmailIfCertifiedByAuthKey(authKey, LocalDateTime.now());

        if (Objects.isNull(email)) {
            log.debug("APP :: {} << 키 값이 잘못되었습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, EmailKeyResponse.invalidKey(), homeKeyboard, callbackFactory.create(telegramId, HOME)));
            return;
        }

        handleEmail(messageVo, email, homeKeyboard);
    }

    private void handleEmail(MessageVo messageVo, String email, HomeKeyboard homeKeyboard) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("APP :: {} << 이메일이 인증되었습니다", telegramId);

        userWriteService.updateEmailByTelegramId(telegramId, email);

        sender().send(SendMessageVo.create(messageVo, EmailKeyResponse.success(), homeKeyboard, callbackFactory.create(telegramId, HOME)));
    }
}
