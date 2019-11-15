package com.podo.helloprice.telegram.client.menu.itemsearchresult;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.pooler.target.danawa.DanawaPooler;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.itemserachadd.ItemSearchAddResponse;
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
public class ItemSearchResultMenuHandler extends AbstractMenuHandler {

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_SEARCH_RESULT;
    }

    private final DanawaPooler danawaPooler;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    public void handle(TMessageVo tMessageVo, String requestMessage) {

        final String telegramId = tMessageVo.getTelegramId() + "";

        log.info("{} << 상품 검색 결과 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));


        //기본 명령어 검증
        final ItemSearchResultCommand requestCommand = ItemSearchResultCommand.from(requestMessage);
        if (Objects.nonNull(requestCommand)) {
            handleCommand(requestCommand, tMessageVo, itemCommands);
            return;
        }


        //상품 명령어인지 검증
        final String itemCode = ItemCommandTranslator.getItemCodeFromCommand(requestMessage);
        if (Objects.isNull(itemCode)) {
            log.info("{} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, requestMessage);
            getSender().send(tMessageVo.newValue(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }


        handleItemCommand(itemCode, tMessageVo, itemCommands);
    }

    private void handleCommand(ItemSearchResultCommand requestCommand, TMessageVo tMessageVo, List<String> itemCommands) {
        switch (requestCommand) {
            case EXIT:
                getSender().send(tMessageVo.newValue(CommonResponse.toHome(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId() + "", Menu.HOME)));
        }
    }

    private void handleItemCommand(String itemCode, TMessageVo tMessageVo, List<String> itemCommands) {

        final String telegramId = tMessageVo.getTelegramId() + "";

        getSender().send(tMessageVo.newValue(CommonResponse.justWait(), null, callbackFactory.createDefault(telegramId, null)));

        final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);
        if (Objects.isNull(itemInfoVo)) {
            getSender().send(tMessageVo.newValue(ItemSearchAddResponse.cantPoolItem(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId() + "", Menu.HOME)));
            return;
        }

        getSender().send(tMessageVo.newValue(CommonResponse.descItemInfoVo(itemInfoVo), itemInfoVo.getItemImage(), km.getItemSearchAddKeyboard(itemCode), callbackFactory.createDefault(telegramId, Menu.ITEM_SEARCH_ADD)));
        getSender().send(tMessageVo.newValue(ItemSearchAddResponse.explain(), null, null, callbackFactory.createDefault(telegramId, null)));
    }

}