package com.podo.helloprice.telegram.client.menu.itemserachadd;

import com.podo.helloprice.core.model.Menu;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.vo.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.Keyboard;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import com.podo.helloprice.telegram.client.menu.global.ItemAddHandler;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemSearchAddMenuHandler extends AbstractMenuHandler {

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_SEARCH_ADD;
    }

    private final ItemAddHandler itemAddHandler;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;

    public void handle(TMessageVo tMessageVo, String requestMessage) {

        final String telegramId = tMessageVo.getTelegramId();

        log.info("{} << 상품 검색 추가 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        if (requestMessage.length() < ItemSearchAddCommand.YES.getValue().length()) {
            sender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        final ItemSearchAddCommand requestCommand = ItemSearchAddCommand.from(requestMessage.substring(0, ItemSearchAddCommand.COMMAND_LENGTH));
        if (Objects.isNull(requestCommand)) {
            sender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        handleCommand(requestCommand, tMessageVo, requestMessage, itemCommands);
    }

    private void handleCommand(ItemSearchAddCommand requestCommand, TMessageVo tMessageVo, String requestMessage, List<String> itemCommands) {
        switch (requestCommand) {
            case YES:
                final int itemCodeValueBeginIndex = ItemSearchAddCommand.COMMAND_LENGTH + 1;
                final String itemCode = requestMessage.substring(itemCodeValueBeginIndex).replace("#", "");
                itemAddHandler.handleItemAdd(tMessageVo, itemCode);
                break;
            case NO:
                sender().send(tMessageVo.newMessage(CommonResponse.toHome(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(tMessageVo.getTelegramId(), Menu.HOME)));
        }
    }

}
