package com.podo.helloprice.telegram.app.menu.email.add;

import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.email.auth.EmailKeyResponse;
import com.podo.helloprice.telegram.app.value.MessageVo;
import com.podo.helloprice.telegram.app.value.SendMessageVo;
import com.podo.helloprice.telegram.domain.user.application.UserReadService;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.global.email.EmailAuthKeyStore;
import com.podo.helloprice.telegram.global.infra.gmail.GmailClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.telegram.app.menu.Menu.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailAddMenuHandler extends AbstractMenuHandler {

    private final UserReadService userReadService;

    private final EmailAuthKeyStore emailAuthKeyStore;
    private final GmailClient gmailClient;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return EMAIL_ADD;
    }

    @Override
    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();
        final String email = messageContents;

        log.debug("TELEGRAM  :: {} << 이메일 추가 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        if (!EmailValidator.getInstance().isValid(email)) {
            log.debug("TELEGRAM  :: {} << 이메일 형식이 아닙니다, 받은메세지 '{}'", telegramId, messageContents);
            sender().send(SendMessageVo.create(messageVo, EmailAddResponse.invalidEmail(), createHomeKeyboard(telegramId), callbackFactory.create(telegramId, HOME)));
            return;
        }

        handleEmailAdd(messageVo, email);
    }

    private void handleEmailAdd(MessageVo messageVo, String email) {
        final String telegramId = messageVo.getTelegramId();

        final UserDetailDto user = userReadService.findByTelegramId(telegramId);

        final String key = emailAuthKeyStore.createAuthKey(email);
        final String title = "이메일 인증을 해주세요!";
        final String content = "KEY : " + key;

        log.debug("TELEGRAM :: {} << 이메일로 KEY 를 전송합니다, 이메일 '{}', KEY '{}'", telegramId, email, key);

        gmailClient.sendEmail(user.getUsername(), email, title, content);

        sender().send(SendMessageVo.create(messageVo, EmailKeyResponse.explain(), Keyboard.TYPING_KEYBOARD, callbackFactory.create(telegramId, EMAIL_KEY)));
    }

}
