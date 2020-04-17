package com.podo.helloprice.telegram.app.menu.product.search;

import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductSearchCrawler;
import com.podo.helloprice.crawl.worker.vo.ProductSearchVo;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.KeyboardHelper;
import com.podo.helloprice.telegram.domain.user.model.Menu;
import com.podo.helloprice.telegram.app.menu.product.ProductCommonResponse;
import com.podo.helloprice.telegram.app.menu.product.ProductDescCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.ProductSearchCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.searchselect.ProductSearchSelectResponse;
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
        return Menu.PRODUCT_SEARCH;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("TELEGRAM :: {} << 상품 검색 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        sender().send(SendMessageVo.create(messageVo, ProductCommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final String requestSearchKeyword = messageContents;
        final List<ProductSearchVo> productSearchResults = danawaProductSearchCrawler.crawl(requestSearchKeyword);

        if (productSearchResults.isEmpty()) {
            final List<String> productCommands = ProductDescCommandTranslator.encodes(userProductNotifyService.findNotifyProductsByUserTelegramId(telegramId));
            sender().send(SendMessageVo.create(messageVo, ProductSearchResponse.noResult(), KeyboardHelper.getHomeKeyboard(productCommands), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        final List<String> productSearchResultCommands = toProductSearchResultCommands(productSearchResults);
        final ReplyKeyboard productSearchResultKeyboard = KeyboardHelper.getProductSearchResultKeyboard(productSearchResultCommands);
        final SentCallback<Message> defaultCallback = callbackFactory.create(telegramId, Menu.PRODUCT_SEARCH_RESULT);
        sender().send(SendMessageVo.create(messageVo, ProductSearchSelectResponse.explain(), productSearchResultKeyboard, defaultCallback));
    }

    private List<String> toProductSearchResultCommands(List<ProductSearchVo> productSearchResults) {
        return productSearchResults.stream()
                .map(product -> ProductSearchCommandTranslator.encode(product.getProductCode(), product.getDescription()))
                .collect(Collectors.toList());
    }

}
