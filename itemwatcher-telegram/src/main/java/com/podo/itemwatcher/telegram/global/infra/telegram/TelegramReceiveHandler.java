package com.podo.itemwatcher.telegram.global.infra.telegram;

import com.podo.itemwatcher.core.domain.user.MenuStatus;
import com.podo.itemwatcher.core.domain.user.UserStatus;
import com.podo.itemwatcher.core.util.MyHttpUtils;
import com.podo.itemwatcher.telegram.domain.ItemService;
import com.podo.itemwatcher.telegram.domain.UserDto;
import com.podo.itemwatcher.telegram.domain.UserService;
import com.podo.itemwatcher.telegram.global.infra.telegram.command.HomeCommand;
import com.podo.itemwatcher.telegram.global.infra.telegram.keyboard.KeyboardManager;
import com.podo.itemwatcher.telegram.global.infra.telegram.message.MessageMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TelegramReceiveHandler {

    public static final String[] ITEM_CODE_PARAMKEY = {"pcode", "code"};

    private final UserService userService;
    private final ItemService itemService;
    private final KeyboardManager keyboardManager;

    private TelegramBot telegramBot;

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void receiveMessage(Update update) {

        Message message = update.getMessage();
        if (Objects.nonNull(update.getEditedMessage())) {
            message = update.getEditedMessage();
        }

        final User user = message.getFrom();
        final String username = user.getLastName() + " " + user.getFirstName();
        final Integer telegramId = user.getId();
        final Integer messageId = message.getMessageId();
        final String messageText = message.getText();

        UserDto.detail userDetail = userService.findByTelegramId(telegramId);

        if (Objects.isNull(userDetail)) {
            insertNewUser(username, telegramId);
            telegramBot.send(telegramId, messageId, MessageMaker.introduce(username), keyboardManager.getHomeKeyboard());
            return;
        }

        handleCommand(telegramId, messageId, messageText, userDetail);

    }


    private void insertNewUser(String username, Integer telegramId) {
        UserDto.insert userInsert = UserDto.insert.builder()
                .username(username)
                .telegramId(telegramId)
                .email(null)
                .menuStatus(MenuStatus.HOME)
                .userStatus(UserStatus.ALIVE)
                .build();

        userService.insert(userInsert);

    }

    private void handleCommand(Integer telegramId, Integer messageId, String message, UserDto.detail userDetail) {
        switch (userDetail.getMenuStatus()) {
            case HOME:
                handleHome(telegramId, messageId, message);
                break;
            case ADD_ITEM:
                handleAddItem(telegramId, messageId, message);
                break;
            case DELETE_ITEM:
                break;
        }
    }

    private void handleAddItem(Integer telegramId, Integer messageId, String message) {
        final String url = message;

        final String itemCode = getItemCode(url);

        if (Objects.isNull(itemCode)) {
            telegramBot.send(telegramId, messageId, MessageMaker.wrongItemUrl(url), keyboardManager.getHomeKeyboard());
        }

    }

    private String getItemCode(String url) {
        String itemCode;

        for (String key : ITEM_CODE_PARAMKEY) {
            itemCode = MyHttpUtils.getParamValue(url, key);
            if (Objects.nonNull(itemCode)) {
                return itemCode;
            }
        }

        return null;
    }

    private void handleHome(Integer telegramId, Integer messageId, String message) {
        HomeCommand homeCommand = HomeCommand.from(message);

        if (Objects.isNull(homeCommand)) {
            telegramBot.send(telegramId, messageId, MessageMaker.wrongInput(), keyboardManager.getHomeKeyboard());
            return;
        }

        switch (homeCommand) {
            case ITEM_ADD:
                userService.updateMenuStatus(telegramId, MenuStatus.ADD_ITEM);
                telegramBot.send(telegramId, messageId, MessageMaker.explainAddItem(), keyboardManager.getDefaultKeyboard());
                break;
            case ITEM_DELETE:
                userService.updateMenuStatus(telegramId, MenuStatus.DELETE_ITEM);
                telegramBot.send(telegramId, messageId, MessageMaker.explainDeleteItem(), keyboardManager.getHomeKeyboard());
                break;
            default:
                telegramBot.send(telegramId, messageId, MessageMaker.wrongInput(), keyboardManager.getHomeKeyboard());
                break;
        }

    }

}
