package com.podo.helloprice.telegram.client.menu.itemsearch;

import com.podo.helloprice.core.domain.item.ItemSearchResultVo;
import com.podo.helloprice.core.domain.model.Menu;
import com.podo.helloprice.crawler.target.danawa.DanawaCrawler;
import com.podo.helloprice.telegram.client.menu.Keyboard;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.itemsearchresult.ItemSearchResultResponse;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemSearchMenuHandler extends AbstractMenuHandler {

    private final UserItemNotifyService userItemNotifyService;
    private final DanawaCrawler danawaCrawler;
    private final TMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_SEARCH;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId();

        log.info("{} << 상품 검색 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        sender().send(tMessageVo.newMessage(CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final String requestSearchKeyword = requestMessage;
        final List<ItemSearchResultVo> itemSearchResults = danawaCrawler.crawlItemSearchResults(requestSearchKeyword);

        if (itemSearchResults.isEmpty()) {
            final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));
            sender().send(tMessageVo.newMessage(ItemSearchResponse.noResult(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        final List<String> itemSearchResultCommands = toItemSearchResultCommands(itemSearchResults);
        final ReplyKeyboard itemSearchResultKeyboard = Keyboard.getItemSearchResultKeyboard(itemSearchResultCommands);
        final SentCallback<Message> defaultCallback = callbackFactory.createDefault(telegramId, Menu.ITEM_SEARCH_RESULT);
        sender().send(tMessageVo.newMessage(ItemSearchResultResponse.explain(), itemSearchResultKeyboard, defaultCallback));
    }

    private List<String> toItemSearchResultCommands(List<ItemSearchResultVo> itemSearchResults) {
        return itemSearchResults.stream()
                .map(item -> ItemCommandTranslator.getItemCommand(item.getItemCode(), item.getItemDesc()))
                .collect(Collectors.toList());
    }

}
