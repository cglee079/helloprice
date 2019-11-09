package com.podo.itemwatcher.telegram.global.infra.telegram;

import com.podo.itemwatcher.core.domain.user.MenuStatus;
import com.podo.itemwatcher.core.domain.user.UserStatus;
import com.podo.itemwatcher.core.util.MyHttpUtils;
import com.podo.itemwatcher.pooler.DanawaPooler;
import com.podo.itemwatcher.core.domain.item.ItemInfoVo;
import com.podo.itemwatcher.telegram.domain.item.ItemDto;
import com.podo.itemwatcher.telegram.domain.item.ItemService;
import com.podo.itemwatcher.telegram.domain.user.UserDto;
import com.podo.itemwatcher.telegram.domain.user.UserService;
import com.podo.itemwatcher.telegram.global.infra.telegram.command.HomeCommand;
import com.podo.itemwatcher.telegram.global.infra.telegram.keyboard.KeyboardManager;
import com.podo.itemwatcher.telegram.global.infra.telegram.message.MessageMaker;
import com.podo.itemwatcher.telegram.global.infra.telegram.message.UserItemCommandMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TelegramReceiveHandler {
    public static final String ITEM_PAGE_URL = "http://prod.danawa.com/info/?pcode=";
    public static final String[] ITEM_CODE_PARAMKEY = {"pcode", "code"};

    private final DanawaPooler danawaPooler;
    private final UserService userService;
    private final UserItemCommandMaker userItemCommandMaker;
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
            telegramBot.send(telegramId, messageId, MessageMaker.introduce(username), keyboardManager.getHomeKeyboard(Collections.emptyList()));
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
        userService.updateMenuStatus(telegramId, MenuStatus.HOME);

        final String url = message;
        final String itemCode = getItemCode(url);

        List<String> itemCommands = userItemCommandMaker.getItemCommands(itemService.findByUserTelegramId(telegramId));

        //URL에서 아이템코드를 찾을 수 없음
        if (Objects.isNull(itemCode)) {
            telegramBot.send(telegramId, messageId, MessageMaker.wrongItemUrl(url), keyboardManager.getHomeKeyboard(itemCommands));
            return;
        }


        final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);

        if (Objects.isNull(itemInfoVo)) {
            telegramBot.send(telegramId, messageId, MessageMaker.wrongItemUrl(url), keyboardManager.getHomeKeyboard(itemCommands));
            return;
        }

        final ItemDto.insert itemInsert = ItemDto.insert.builder()
                .itemCode(itemCode)
                .itemUrl(ITEM_PAGE_URL + itemCode)
                .itemName(itemInfoVo.getItemName())
                .itemImage(itemInfoVo.getItemImage())
                .itemPrice(itemInfoVo.getItemPrice())
                .itemSaleStatus(itemInfoVo.getItemSaleStatus())
                .build();

        final Long itemId = itemService.insertIfNotExist(itemInsert);
        ItemDto.detail itemDetail = itemService.findByItemId(itemId);

        if (userService.hasNotifyItem(telegramId, itemId)) {
            telegramBot.send(telegramId, messageId, MessageMaker.alreadySetNotifyItem(itemDetail), itemDetail.getItemImage(), keyboardManager.getHomeKeyboard(itemCommands));
            return;
        }

        userService.addNotifyItem(telegramId, itemId);
        itemCommands = userItemCommandMaker.getItemCommands(itemService.findByUserTelegramId(telegramId)); // 갱신
        telegramBot.send(telegramId, messageId, MessageMaker.successAddItem(itemDetail), itemInfoVo.getItemImage(), keyboardManager.getHomeKeyboard(itemCommands));
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

        List<String> itemCommands = userItemCommandMaker.getItemCommands(itemService.findByUserTelegramId(telegramId));


        if (Objects.isNull(homeCommand)) {

            //아이템 명령어인지 체크,
            final String itemCode = userItemCommandMaker.getItemCodeFromCommand(message);

            // 잘못된 명령어
            if (Objects.isNull(itemCode)) {
                telegramBot.send(telegramId, messageId, MessageMaker.wrongInput(), keyboardManager.getHomeKeyboard(itemCommands));
                return;
            }

            final ItemDto.detail itemDetail = itemService.findByItemCode(itemCode);

            //아이템 코드가 잘못된 경우
            if (Objects.isNull(itemDetail)) {
                telegramBot.send(telegramId, messageId, MessageMaker.wrongItemCode(itemCode), keyboardManager.getHomeKeyboard(itemCommands));
                return;
            }

            //정상 아이템 명령어
            telegramBot.send(telegramId, messageId, MessageMaker.makeItemInfo(itemDetail), itemDetail.getItemImage(), keyboardManager.getHomeKeyboard(itemCommands));

            return;
        }


        switch (homeCommand) {
            case ADD_ITEM:
                userService.updateMenuStatus(telegramId, MenuStatus.ITEM_ADD);
                telegramBot.send(telegramId, messageId, MessageMaker.explainAddItem(), keyboardManager.getDefaultKeyboard());
                break;
            case DELETE_ITEM:
                userService.updateMenuStatus(telegramId, MenuStatus.ITEM_DELETE);
                telegramBot.send(telegramId, messageId, MessageMaker.explainDeleteItem(), keyboardManager.getHomeKeyboard(itemCommands));
                break;
            default:
                telegramBot.send(telegramId, messageId, MessageMaker.wrongInput(), keyboardManager.getHomeKeyboard(itemCommands));
                break;
        }

    }

}
