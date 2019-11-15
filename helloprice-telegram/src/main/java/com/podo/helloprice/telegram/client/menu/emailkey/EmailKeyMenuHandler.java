package com.podo.helloprice.telegram.client.menu.emailkey;

import com.podo.helloprice.core.domain.item.ItemSearchResultVo;
import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.client.menu.itemsearchresult.ItemSearchResultResponse;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.global.email.EmailKeyStore;
import com.podo.helloprice.telegram.global.infra.gmail.GmailClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailKeyMenuHandler extends AbstractMenuHandler {

    private final EmailKeyStore emailKeyStore;
    private final UserItemNotifyService userItemNotifyService;
    private final UserService userService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    @Override
    public Menu getHandleMenu() {
        return Menu.EMAIL_KEY;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        log.info("{} << 이메일 인증 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final String key = requestMessage;

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        final LocalDateTime now = LocalDateTime.now();
        final String email = emailKeyStore.certifyKey(key, now);

        if (Objects.isNull(email)) {
            log.info("{} << 키 값이 잘못되었습니다. 받은메세지 '{}'", telegramId, key);
            getSender().send(tMessageVo.newValue(EmailKeyResponse.invalidKey(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }


        log.info("{} << 이메일이 인증되었습니다. 받은메세지 '{}'", telegramId, key);
        userService.updateEmail(telegramId, email);
        getSender().send(tMessageVo.newValue(EmailKeyResponse.success(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));

    }

}
