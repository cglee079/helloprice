package com.podo.helloprice.telegram.client.menu.itemsearchresult;

import com.podo.helloprice.core.domain.item.vo.CrawledItem;
import com.podo.helloprice.core.model.Menu;
import com.podo.helloprice.telegram.client.menu.Keyboard;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.vo.TMessageVo;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.itemserachadd.ItemSearchAddResponse;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.global.cache.DanawaItemCache;
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

    private final DanawaItemCache danawaItemCache;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId();
        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        log.info("{} << 상품 검색 결과 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final ItemSearchResultCommand requestCommand = ItemSearchResultCommand.from(requestMessage);
        if (Objects.nonNull(requestCommand)) {
            handleCommand(requestCommand, tMessageVo, itemCommands);
            return;
        }

        final String itemCode = ItemCommandTranslator.getItemCodeFromCommand(requestMessage);
        if (Objects.isNull(itemCode)) {
            log.info("{} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }


        handleItemCommand(itemCode, tMessageVo, itemCommands);
    }

    private void handleCommand(ItemSearchResultCommand requestCommand, TMessageVo tMessageVo, List<String> itemCommands) {
        switch (requestCommand) {
            case EXIT:
                sender().send(tMessageVo.newMessage(CommonResponse.toHome(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(tMessageVo.getTelegramId(), Menu.HOME)));
        }
    }

    private void handleItemCommand(String itemCode, TMessageVo tMessageVo, List<String> itemCommands) {
        final String telegramId = tMessageVo.getTelegramId();

        sender().send(tMessageVo.newMessage(CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final CrawledItem crawledItem = danawaItemCache.get(itemCode);
        if (Objects.isNull(crawledItem)) {
            sender().send(tMessageVo.newMessage(ItemSearchAddResponse.failPoolItem(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(tMessageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        sender().send(tMessageVo.newMessage(CommonResponse.descCrawledItemVo(crawledItem), crawledItem.getItemImage(), Keyboard.getItemSearchAddKeyboard(itemCode), callbackFactory.create(telegramId, Menu.ITEM_SEARCH_ADD)));
        sender().send(tMessageVo.newMessage(ItemSearchAddResponse.explain(), null, null, callbackFactory.createDefaultNoAction(telegramId)));
    }

}
