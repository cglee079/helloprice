package com.podo.helloprice.telegram.client.menu.itemdelete;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
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

        log.info("{} << 상품 알림 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        ItemDeleteCommand itemDeleteCommand = ItemDeleteCommand.from(requestMessage);
        if (Objects.nonNull(itemDeleteCommand)) {
            handleCommand(itemDeleteCommand, tMessageVo, itemCommands);
            return;
        }

        final String itemCode = ItemCommandTranslator.getItemCodeFromCommand(requestMessage);

        if (Objects.isNull(itemCode)) {
            log.info("{} << 잘못된 값을 입력했습니다. 상품코드를 찾을 수 없습니다. 받은메세지 '{}'", telegramId, requestMessage);
            getSender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        final UserDto.detail userDetail = userService.findByTelegramId(telegramId);
        final ItemDto.detail itemDetail = itemService.findByItemCode(itemCode);

        if (!userItemNotifyService.hasNotify(userDetail.getId(), itemDetail.getId())) {
            log.info("{} << 삭제 요청한 {}({}) 상품 알림이 등록되어있지 않습니다. 받은메세지 '{}'", telegramId, itemDetail.getItemName(), itemCode, requestMessage);
            getSender().send(tMessageVo.newMessage(ItemDeleteResponse.alreadyNotNotifyItem(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
        }

        userItemNotifyService.deleteNotifyByUserIdAndItemId(userDetail.getId(), itemDetail.getId());
        final List<String> reloadItemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));
        getSender().send(tMessageVo.newMessage(ItemDeleteResponse.deletedNotifyItem(itemDetail), km.getHomeKeyboard(reloadItemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));

    }

    private void handleCommand(ItemDeleteCommand itemDeleteCommand, TMessageVo tMessageVo, List<String> itemCommands) {
        switch (itemDeleteCommand) {
            case EXIT:
                getSender().send(tMessageVo.newMessage(CommonResponse.toHome(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId() + "", Menu.HOME)));
        }
    }
}

