package com.podo.helloprice.telegram.client.menu.emailadd;

import com.podo.helloprice.core.domain.model.Menu;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.Keyboard;
import com.podo.helloprice.telegram.client.menu.emailkey.EmailKeyResponse;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.global.email.EmailKeyStore;
import com.podo.helloprice.telegram.global.infra.gmail.GmailClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailAddMenuHandler extends AbstractMenuHandler {

    private final EmailKeyStore emailKeyStore;
    private final GmailClient gmailClient;
    private final UserItemNotifyService userItemNotifyService;
    private final UserService userService;
    private final TMessageCallbackFactory callbackFactory;


    @Override
    public Menu getHandleMenu() {
        return Menu.EMAIL_ADD;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId();
        final String email = requestMessage;

        log.info("{} << 이메일 추가 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);


        if (!EmailValidator.getInstance().isValid(email)) {
            log.info("{} << 이메일 형식이 아닙니다, 받은메세지 '{}'", telegramId, requestMessage);
            final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));
            sender().send(tMessageVo.newMessage(EmailAddResponse.invalidEmail(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        handleEmailAdd(tMessageVo, email);
    }

    private void handleEmailAdd(TMessageVo tMessageVo, String email) {
        final String telegramId = tMessageVo.getTelegramId();

        final UserDto.detail existedUser = userService.findByTelegramId(telegramId);

        final String key = emailKeyStore.createAuthKey(email);
        final String title = "이메일 인증을 해주세요!";
        final String content = "KEY : " + key;

        log.info("{} << 이메일로 KEY를 전송합니다, 이메일 '{}', KEY '{}'", telegramId, email, key);
        gmailClient.sendEmail(existedUser.getUsername(), email, title, content);
        sender().send(tMessageVo.newMessage(EmailKeyResponse.explain(), Keyboard.getDefaultKeyboard(), callbackFactory.createDefault(telegramId, Menu.EMAIL_KEY)));
    }

}
