package com.podo.helloprice.telegram.client.menu.itemserachadd;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.telegram.client.KeyboardManager;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.UserItemCommand;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.global.ItemAddHandler;
import com.podo.helloprice.telegram.client.response.CommonResponse;
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

        final List<String> itemCommands = UserItemCommand.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        final ItemSearchAddCommand requestCommand = ItemSearchAddCommand.from(requestMessage.substring(0, 3));

        if (Objects.nonNull(requestCommand)) {
            handleCommand(requestCommand, tMessageVo, requestMessage, itemCommands);
            return;
        }

        getSender().send(tMessageVo.newValue(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
    }

    private void handleCommand(ItemSearchAddCommand requestCommand, TMessageVo tMessageVo, String requestMessage, List<String> itemCommands) {
        switch (requestCommand) {
            case YES:
                final String itemCode = requestMessage.substring(4).replace("#", "");
                itemAddHandler.handleItemAdd(tMessageVo, itemCode);
                break;
            case NO:
                getSender().send(tMessageVo.newValue(CommonResponse.toHome(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId() + "", Menu.HOME)));
        }
    }

}
