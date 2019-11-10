package com.podo.sadream.telegram.client;

import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.core.domain.user.UserStatus;
import com.podo.sadream.telegram.client.response.CommonResponse;
import com.podo.sadream.telegram.domain.user.UserDto;
import com.podo.sadream.telegram.domain.user.UserService;
import com.podo.sadream.telegram.client.menu.MenuHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final UserService userService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;
    private final Map<Menu, MenuHandler> menuHandlers;

    @PostConstruct
    public void setTelegramBot() {
        for (Menu menu : menuHandlers.keySet()) {
            menuHandlers.get(menu).setBot(this);
        }
    }

    @Value("${telegram.podo_sadream.bot.token}")
    private String botToken;

    @Value("${telegram.podo_sadream.bot.name}")
    private String botUsername;

    @Value("${telegram.podo_sadream.admin.id}")
    private String adminId;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (Objects.nonNull(update.getEditedMessage())) {
            message = update.getEditedMessage();
        }

        final User user = message.getFrom();
        final String username = user.getLastName() + " " + user.getFirstName();
        final Integer telegramId = user.getId();
        final Integer messageId = message.getMessageId();
        final String messageText = message.getText();

        log.info("{} << 메세지 수신 : {}", username, messageText);

        final TMessageVo tMessageVo = new TMessageVo(telegramId, messageId);

        final UserDto.detail userDetail = userService.findByTelegramId(telegramId + "");

        if (Objects.isNull(userDetail)) {
            insertNewUser(username, telegramId);
            this.send(tMessageVo.newValue(CommonResponse.introduce(username), km.getHomeKeyboard(Collections.emptyList()), callbackFactory.createDefaultCallback(telegramId + "", Menu.HOME)));
            return;
        }

        handleCommand(tMessageVo, messageText, userDetail.getMenuStatus());
    }


    private void insertNewUser(String username, Integer telegramId) {
        log.info("{} << 새로운 사용자가 등록되었습니다 {}({})", telegramId, username, telegramId);

        final UserDto.insert userInsert = UserDto.insert.builder()
                .username(username)
                .telegramId(telegramId)
                .email(null)
                .menuStatus(Menu.HOME)
                .userStatus(UserStatus.ALIVE)
                .build();

        userService.insert(userInsert);
    }

    private void handleCommand(TMessageVo tMessageVo, String requestMessage, Menu userMenuStatus) {
        MenuHandler menuHandler = menuHandlers.get(userMenuStatus);
        menuHandler.handle(tMessageVo, requestMessage);
    }

    public void send(TMessageVo tMessageVo) {

        final String image = tMessageVo.getImage();

        if (StringUtils.isEmpty(image)) {
            sendMessage(tMessageVo);
            return;
        }

        final SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(tMessageVo.getTelegramId().toString());
        sendPhoto.setPhoto(image);
        sendPhoto.setReplyMarkup(tMessageVo.getKeyboard());
        sendPhoto.setReplyToMessageId(tMessageVo.getMessageId());

        try {
            this.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("이미지를 전송 할 수 없습니다. Image : {}", image);
        }

        sendMessage(tMessageVo);
    }

    private void sendMessage(TMessageVo tMessageVo) {
        final SendMessage sendMessage = new SendMessage(tMessageVo.getTelegramId().toString(), tMessageVo.getMessage());

        sendMessage.setReplyMarkup(tMessageVo.getKeyboard());
        sendMessage.setReplyToMessageId(tMessageVo.getMessageId());
        sendMessage.enableHtml(true);

        try {
            this.executeAsync(sendMessage, tMessageVo.getCallback());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
