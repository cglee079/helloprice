package com.podo.helloprice.telegram.app.menu.productsearch;

import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductSearchCrawler;
import com.podo.helloprice.crawl.worker.vo.ProductSearchVo;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.Keyboard;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.app.menu.global.CommonResponse;
import com.podo.helloprice.telegram.app.menu.global.ProductCommandTranslator;
import com.podo.helloprice.telegram.app.menu.productsearchresult.ProductSearchResultResponse;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
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
public class ProductSearchMenuHandler extends AbstractMenuHandler {

    private final UserProductNotifyService userProductNotifyService;
    private final DanawaProductSearchCrawler danawaProductSearchCrawler;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_SEARCH;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("TELEGRAM :: {} << 상품 검색 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        sender().send(SendMessageVo.create(messageVo, CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final String requestSearchKeyword = messageContents;
        final List<ProductSearchVo> productSearchResults = danawaProductSearchCrawler.crawl(requestSearchKeyword);

        if (productSearchResults.isEmpty()) {
            final List<String> productCommands = ProductCommandTranslator.getProductCommands(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));
            sender().send(SendMessageVo.create(messageVo, ProductSearchResponse.noResult(), Keyboard.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        final List<String> productSearchResultCommands = toProductSearchResultCommands(productSearchResults);
        final ReplyKeyboard productSearchResultKeyboard = Keyboard.getProductSearchResultKeyboard(productSearchResultCommands);
        final SentCallback<Message> defaultCallback = callbackFactory.create(telegramId, Menu.ITEM_SEARCH_RESULT);
        sender().send(SendMessageVo.create(messageVo, ProductSearchResultResponse.explain(), productSearchResultKeyboard, defaultCallback));
    }

    private List<String> toProductSearchResultCommands(List<ProductSearchVo> productSearchResults) {
        return productSearchResults.stream()
                .map(product -> ProductCommandTranslator.getProductCommand(product.getProductCode(), product.getDescription()))
                .collect(Collectors.toList());
    }

}
