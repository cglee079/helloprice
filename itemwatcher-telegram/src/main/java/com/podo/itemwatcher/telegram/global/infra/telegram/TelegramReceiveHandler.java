package com.podo.itemwatcher.telegram.global.infra.telegram;

import com.podo.itemwatcher.core.domain.user.MenuStatus;
import com.podo.itemwatcher.core.domain.user.UserStatus;
import com.podo.itemwatcher.core.util.MyHttpUtils;
import com.podo.itemwatcher.pooler.DanawaPooler;
import com.podo.itemwatcher.pooler.domain.ItemInfoVo;
import com.podo.itemwatcher.telegram.domain.ItemDto;
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

    private final DanawaPooler danawaPooler;
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
            case ITEM_ADD:
                handleAddItem(telegramId, messageId, message);
                break;
            case ITEM_DELETE:
                break;
        }
    }

    private void handleAddItem(Integer telegramId, Integer messageId, String message) {
        final String url = message;

        final String itemCode = getItemCode(url);

        if (Objects.isNull(itemCode)) {
            userService.updateMenuStatus(telegramId, MenuStatus.HOME);
            telegramBot.send(telegramId, messageId, MessageMaker.wrongItemUrl(url), keyboardManager.getHomeKeyboard());
            return;
        }

        final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);

        final ItemDto.insert itemInsert = ItemDto.insert.builder()
                .itemCode(itemCode)
                .itemName(itemInfoVo.getItemName())
                .itemImage(itemInfoVo.getItemImage())
                .itemPrice(itemInfoVo.getItemPrice())
                .build();

        final Long itemId = itemService.insertIfNotExist(itemInsert);

        if (userService.hasNotifyItem(itemId)) {
            userService.updateMenuStatus(telegramId, MenuStatus.HOME);
            telegramBot.send(telegramId, messageId, MessageMaker.alreadySetNotifyItem(itemInfoVo), keyboardManager.getHomeKeyboard());
            return;
        }

        userService.addNotifyItem(telegramId, itemId);
        userService.updateMenuStatus(telegramId, MenuStatus.HOME);
        telegramBot.send(telegramId, messageId, MessageMaker.successAddItem(itemInfoVo), keyboardManager.getHomeKeyboard());
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
            case ADD_ITEM:
                userService.updateMenuStatus(telegramId, MenuStatus.ITEM_ADD);
                telegramBot.send(telegramId, messageId, MessageMaker.explainAddItem(), keyboardManager.getDefaultKeyboard());
                break;
            case DELETE_ITEM:
                userService.updateMenuStatus(telegramId, MenuStatus.ITEM_DELETE);
                telegramBot.send(telegramId, messageId, MessageMaker.explainDeleteItem(), keyboardManager.getHomeKeyboard());
                break;
            default:
                telegramBot.send(telegramId, messageId, MessageMaker.wrongInput(), keyboardManager.getHomeKeyboard());
                break;
        }

    }

}
