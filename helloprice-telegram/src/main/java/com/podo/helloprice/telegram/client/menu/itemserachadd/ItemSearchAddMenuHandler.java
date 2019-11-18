package com.podo.helloprice.telegram.client.menu.itemserachadd;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.global.ItemAddHandler;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
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
    private final KeyboardManager km;

    public void handle(TMessageVo tMessageVo, String requestMessage) {

        final String telegramId = tMessageVo.getTelegramId() + "";

        log.info("{} << 상품 검색 추가 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));


        //기본 명령어보다 글자 수가 작을 경우
        if (requestMessage.length() < ItemSearchAddCommand.YES.getValue().length()) {
            getSender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        //명령어가 아닌 경우
        final ItemSearchAddCommand requestCommand = ItemSearchAddCommand.from(requestMessage.substring(0, ItemSearchAddCommand.COMMAND_LENGTH));
        if (Objects.isNull(requestCommand)) {
            getSender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
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
                getSender().send(tMessageVo.newMessage(CommonResponse.toHome(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId() + "", Menu.HOME)));
        }
    }

}
