package com.podo.helloprice.telegram.client.menu.emailkey;

import com.podo.helloprice.core.domain.model.Menu;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.Keyboard;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.global.email.EmailKeyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailKeyMenuHandler extends AbstractMenuHandler {

    private final EmailKeyStore emailKeyStore;
    private final UserItemNotifyService userItemNotifyService;
    private final UserService userService;
    private final TMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.EMAIL_KEY;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId();
        final String emailKey = requestMessage;

        log.info("{} << 이메일 인증 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        final String email = emailKeyStore.certifyKey(emailKey, LocalDateTime.now());

        if (Objects.isNull(email)) {
            log.info("{} << 키 값이 잘못되었습니다. 받은메세지 '{}'", telegramId, emailKey);
            sender().send(tMessageVo.newMessage(EmailKeyResponse.invalidKey(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        handleEmailKey(tMessageVo, emailKey, itemCommands, email);
    }

    private void handleEmailKey(TMessageVo tMessageVo, String emailKey, List<String> itemCommands, String email) {
        final String telegramId = tMessageVo.getTelegramId();

        log.info("{} << 이메일이 인증되었습니다. 받은메세지 '{}'", telegramId, emailKey);
        userService.updateEmailByTelegramId(telegramId, email);
        sender().send(tMessageVo.newMessage(EmailKeyResponse.success(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
    }

}
