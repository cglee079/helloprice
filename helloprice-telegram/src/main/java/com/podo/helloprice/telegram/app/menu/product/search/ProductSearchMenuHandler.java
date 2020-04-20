package com.podo.helloprice.telegram.app.menu.product.search;

import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductSearchCrawler;
import com.podo.helloprice.crawl.worker.vo.ProductSearchVo;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.product.searchselect.ProductSearchSelectCommandTranslator;
import com.podo.helloprice.telegram.app.menu.product.searchselect.ProductSearchSelectKeyboard;
import com.podo.helloprice.telegram.app.menu.product.searchselect.ProductSearchSelectResponse;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.domain.user.model.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductSearchMenuHandler extends AbstractMenuHandler {

    private final DanawaProductSearchCrawler danawaProductSearchCrawler;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return Menu.PRODUCT_SEARCH;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();

        log.debug("TELEGRAM :: {} << 상품 검색 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        sender().send(SendMessageVo.create(messageVo, CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final String requestSearchKeyword = messageContents;
        final List<ProductSearchVo> productSearchResults = danawaProductSearchCrawler.crawl(requestSearchKeyword);

        if (productSearchResults.isEmpty()) {
            sender().send(SendMessageVo.create(messageVo, ProductSearchResponse.noResult(), getHomeKeyboard(telegramId), callbackFactory.create(messageVo.getTelegramId(), Menu.HOME)));
            return;
        }

        sender().send(
                SendMessageVo.create(
                        messageVo,
                        ProductSearchSelectResponse.explain(),
                        new ProductSearchSelectKeyboard(toProductSearchResultCommands(productSearchResults)),
                        callbackFactory.create(telegramId, Menu.PRODUCT_SEARCH_RESULT)
                )
        );
    }

    private List<String> toProductSearchResultCommands(List<ProductSearchVo> productSearchResults) {
        return productSearchResults.stream()
                .map(product -> ProductSearchSelectCommandTranslator.encode(product.getProductCode(), product.getDescription()))
                .collect(Collectors.toList());
    }

}
