package com.podo.helloprice.telegram.client;


import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.core.domain.user.UserStatus;
import com.podo.helloprice.telegram.client.menu.MenuHandler;
import com.podo.helloprice.telegram.client.response.CommonResponse;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramMessageReceiver {

    private final TelegramMessageSender telegramMessageSender;
    private final UserService userService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;
    private final Map<Menu, MenuHandler> menuHandlers;

    @Value("${app.name}")
    private String appName;

    @Value("${app.telegram.help_url}")
    private String helpUrl;

    public void receive(Message message) {

        final User user = message.getFrom();
        final String username = user.getLastName() + " " + user.getFirstName();
        final Integer telegramId = user.getId();
        final Integer messageId = message.getMessageId();
        final String messageText = message.getText();
        final LocalDateTime messageReceiveAt = LocalDateTime.now();

        log.info("{} << 메세지 수신 : {}", telegramId, messageText);

        final TMessageVo tMessageVo = new TMessageVo(telegramId, messageId);

        final UserDto.detail userDetail = userService.findByTelegramId(telegramId + "");

        if (Objects.isNull(userDetail)) {
            insertNewUser(username, telegramId, messageReceiveAt);
            telegramMessageSender.send(tMessageVo.newValue(CommonResponse.introduce(appName), km.getHomeKeyboard(Collections.emptyList()), callbackFactory.createDefault(telegramId + "", Menu.HOME)));
            telegramMessageSender.send(tMessageVo.newValue(CommonResponse.help(helpUrl), null, callbackFactory.createDefault(telegramId + "", Menu.HOME)));
            return;
        } else if (userDetail.getUserStatus().equals(UserStatus.DEAD)) {
            userService.reviveUser(userDetail.getId());
            userService.updateSendAt(userDetail.getId(), messageReceiveAt);
            telegramMessageSender.send(tMessageVo.newValue(CommonResponse.introduce(appName), km.getHomeKeyboard(Collections.emptyList()), callbackFactory.createDefault(telegramId + "", Menu.HOME)));
            telegramMessageSender.send(tMessageVo.newValue(CommonResponse.help(helpUrl), null, callbackFactory.createDefault(telegramId + "", Menu.HOME)));
            return;
        }

        userService.updateSendAt(userDetail.getId(), messageReceiveAt);
        handleCommand(tMessageVo, messageText, userDetail.getMenuStatus());
    }


    private void insertNewUser(String username, Integer telegramId, LocalDateTime messageReceiveAt) {
        log.info("{} << 새로운 사용자가 등록되었습니다 {}({})", telegramId, username, telegramId);

        final UserDto.insert userInsert = UserDto.insert.builder()
                .username(username)
                .telegramId(telegramId)
                .email(null)
                .menuStatus(Menu.HOME)
                .userStatus(UserStatus.ALIVE)
                .lastSendAt(messageReceiveAt)
                .build();

        userService.insert(userInsert);
    }

    private void handleCommand(TMessageVo tMessageVo, String requestMessage, Menu userMenuStatus) {
        MenuHandler menuHandler = menuHandlers.get(userMenuStatus);
        menuHandler.handle(tMessageVo, requestMessage);
    }
}
