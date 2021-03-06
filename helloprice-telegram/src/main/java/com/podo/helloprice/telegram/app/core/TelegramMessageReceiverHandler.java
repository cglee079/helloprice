package com.podo.helloprice.telegram.app.core;


import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.MenuHandler;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.value.MessageVo;
import com.podo.helloprice.telegram.app.value.SendMessageVo;
import com.podo.helloprice.telegram.domain.tuser.application.TUserReadService;
import com.podo.helloprice.telegram.domain.tuser.application.TUserWriteService;
import com.podo.helloprice.telegram.domain.tuser.dto.TUserDetailDto;
import com.podo.helloprice.telegram.domain.tuser.dto.TUserInsertDto;
import com.podo.helloprice.telegram.app.menu.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Collections.emptyList;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramMessageReceiverHandler {

    @Value("${app.name}")
    private String appName;

    @Value("${app.help_url}")
    private String helpUrl;

    private final TUserReadService userReadService;
    private final TUserWriteService userWriteService;

    private final TelegramMessageSender telegramMessageSender;
    private final SendMessageCallbackFactory callbackFactory;

    private final Map<Menu, MenuHandler> menuHandlers;

    public void receive(Message message) {

        final User telegramUser = message.getFrom();
        final String username = telegramUser.getLastName() + " " + telegramUser.getFirstName();
        final String telegramId = telegramUser.getId() + "";
        final Integer messageId = message.getMessageId();
        final String messageText = message.getText();
        final LocalDateTime messageReceiveAt = LocalDateTime.now();

        log.debug("TELEGRAM :: {} << 메세지 수신 : {}", telegramId, messageText);

        final MessageVo messageVo = new MessageVo(telegramId, messageId);

        if (!userReadService.existedTelegramId(telegramId)) {
            userWriteService.updateSendAt(insertNewUser(username, messageVo), messageReceiveAt);
            return;
        }

        final TUserDetailDto userDetail = userReadService.findByTelegramId(telegramId);

        if (userDetail.getUserStatus().equals(UserStatus.DEAD)) {
            reviveExistedUser(userDetail.getId(), messageVo);
            userWriteService.updateSendAt(userDetail.getId(), messageReceiveAt);
            return;
        }

        userWriteService.updateSendAt(userDetail.getId(), messageReceiveAt);
        handleCommand(messageVo, messageText, userDetail.getMenuStatus());
    }

    private void reviveExistedUser(Long id, MessageVo messageVo) {
        userWriteService.reviveUser(id);
        sendMessageToNewUser(messageVo);
    }

    private Long insertNewUser(String username, MessageVo messageVo) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("APP :: {} << 새로운 사용자가 등록되었습니다 {}({})", telegramId, username, telegramId);

        final TUserInsertDto userInsert = TUserInsertDto.builder()
                .username(username)
                .telegramId(telegramId)
                .email(null)
                .menuStatus(Menu.HOME)
                .userStatus(UserStatus.ALIVE)
                .build();

        final Long userId = userWriteService.insertNewUser(userInsert);

        sendMessageToNewUser(messageVo);

        return userId;
    }

    private void sendMessageToNewUser(MessageVo messageVo) {
        final String telegramId = messageVo.getTelegramId();
        telegramMessageSender.send(SendMessageVo.create(messageVo, CommonResponse.introduce(appName), new HomeKeyboard(emptyList()), callbackFactory.create(telegramId, Menu.HOME)));
        telegramMessageSender.sendWithWebPagePreview(SendMessageVo.create(messageVo, CommonResponse.help(helpUrl), null, callbackFactory.create(telegramId, null)));
        telegramMessageSender.send(SendMessageVo.create(messageVo, CommonResponse.seeKeyboardIcon(), null, callbackFactory.create(telegramId, null)));
    }

    private void handleCommand(MessageVo messageVo, String requestMessage, Menu userMenuStatus) {
        final MenuHandler menuHandler = menuHandlers.get(userMenuStatus);
        menuHandler.handle(messageVo, requestMessage);
    }
}
