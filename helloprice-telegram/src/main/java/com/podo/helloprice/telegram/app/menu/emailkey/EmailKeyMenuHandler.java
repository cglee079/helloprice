package com.podo.helloprice.telegram.app.menu.emailkey;


import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.user.application.UserWriteService;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
import com.podo.helloprice.telegram.global.email.EmailKeyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.podo.helloprice.telegram.app.menu.Menu.EMAIL_KEY;
import static com.podo.helloprice.telegram.app.menu.Menu.HOME;
import static com.podo.helloprice.telegram.app.menu.global.ProductCommandTranslator.getProductCommands;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailKeyMenuHandler extends AbstractMenuHandler {

    private final EmailKeyStore emailKeyStore;
    private final UserProductNotifyService userProductNotifyService;
    private final UserWriteService userWriteService;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return EMAIL_KEY;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();
        final String authKey = messageContents;

        log.debug("TELEGRAM :: {} << 이메일 인증 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        final List<String> productCommands = getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));

        final String email = emailKeyStore.getEmailIfCertifiedByAuthKey(authKey, LocalDateTime.now());

        if (Objects.isNull(email)) {
            log.debug("APP :: {} << 키 값이 잘못되었습니다", telegramId);
            sender().send(SendMessageVo.create(messageVo, EmailKeyResponse.invalidKey(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, HOME)));
            return;
        }

        handleEmail(messageVo, productCommands, email);
    }

    private void handleEmail(MessageVo messageVo, List<String> productCommands, String email) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("APP :: {} << 이메일이 인증되었습니다", telegramId);
        userWriteService.updateEmailByTelegramId(telegramId, email);

        sender().send(SendMessageVo.create(messageVo, EmailKeyResponse.success(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(telegramId, HOME)));
    }

}
