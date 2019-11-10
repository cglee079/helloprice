package com.podo.sadream.telegram.client.menu.itemdelete;

import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.telegram.client.response.CommonResponse;
import com.podo.sadream.telegram.client.TMessageCallbackFactory;
import com.podo.sadream.telegram.domain.item.ItemDto;
import com.podo.sadream.telegram.domain.item.ItemService;
import com.podo.sadream.telegram.domain.user.UserDto;
import com.podo.sadream.telegram.domain.user.UserService;
import com.podo.sadream.telegram.domain.useritem.UserItemNotifyService;
import com.podo.sadream.telegram.client.TMessageVo;
import com.podo.sadream.telegram.client.menu.AbstractMenuHandler;
import com.podo.sadream.telegram.client.KeyboardManager;
import com.podo.sadream.telegram.client.UserItemCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemDeleteMenuHandler extends AbstractMenuHandler {

    private final ItemService itemService;
    private final UserService userService;
    private final UserItemNotifyService userItemNotifyService;

    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_DELETE;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        log.info("{} << 상품 알림 삭제 메뉴에서 응답, RequestMessage '{}'", telegramId, requestMessage);

        final List<String> itemCommands = UserItemCommand.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        ItemDeleteCommand itemDeleteCommand = ItemDeleteCommand.from(requestMessage);
        if (Objects.nonNull(itemDeleteCommand)) {
            handleCommand(itemDeleteCommand, tMessageVo, itemCommands);
            return;
        }

        final String itemCode = UserItemCommand.getItemCodeFromCommand(requestMessage);

        if (Objects.isNull(itemCode)) {
            log.info("{} << 잘못된 값을 입력했습니다. 상품코드를 찾을 수 없습니다. RequestMessage '{}'", telegramId, requestMessage);
            getBot().send(tMessageVo.newValue(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
            return;
        }

        final UserDto.detail userDetail = userService.findByTelegramId(telegramId);
        final ItemDto.detail itemDetail = itemService.findByItemCode(itemCode);

        if (!userItemNotifyService.hasNotify(userDetail.getId(), itemDetail.getId())) {
            log.info("{} << 삭제 요청한 {}({}) 상품 알림이 등록되어있지 않습니다. RequestMessage '{}'", telegramId, itemDetail.getItemName(), itemCode, requestMessage);
            getBot().send(tMessageVo.newValue(ItemDeleteResponse.alreadyNotNotifyItem(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
        }

        userItemNotifyService.deleteNotify(userDetail.getId(), itemDetail.getId());
        final List<String> reloadItemCommands = UserItemCommand.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));
        getBot().send(tMessageVo.newValue(ItemDeleteResponse.deletedNotifyItem(itemDetail), km.getHomeKeyboard(reloadItemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));

    }

    private void handleCommand(ItemDeleteCommand itemDeleteCommand, TMessageVo tMessageVo, List<String> itemCommands) {
        switch (itemDeleteCommand) {
            case EXIT:
                getBot().send(tMessageVo.newValue(CommonResponse.exit(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(tMessageVo.getTelegramId() + "", Menu.HOME)));
                return;
            default:
                return;
        }
    }
}

