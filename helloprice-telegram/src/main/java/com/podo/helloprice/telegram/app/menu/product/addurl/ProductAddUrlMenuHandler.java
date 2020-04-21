package com.podo.helloprice.telegram.app.menu.product.addurl;


import com.podo.helloprice.crawl.worker.target.danawa.DanawaProductCodeCrawler;
import com.podo.helloprice.telegram.app.SendMessageCallbackFactory;
import com.podo.helloprice.telegram.app.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.app.menu.CommonResponse;
import com.podo.helloprice.telegram.app.menu.product.global.ProductAddHandler;
import com.podo.helloprice.telegram.app.vo.MessageVo;
import com.podo.helloprice.telegram.app.vo.SendMessageVo;
import com.podo.helloprice.telegram.app.menu.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.podo.helloprice.telegram.app.menu.Menu.PRODUCT_ADD;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductAddUrlMenuHandler extends AbstractMenuHandler {

    private final ProductAddHandler productAddHandler;
    private final DanawaProductCodeCrawler danawaProductCodeCrawler;
    private final SendMessageCallbackFactory callbackFactory;

    @Override
    public Menu getHandleMenu() {
        return PRODUCT_ADD;
    }

    public void handle(MessageVo messageVo, String messageContents) {
        final String telegramId = messageVo.getTelegramId();
        final String requestUrl = messageContents;

        log.debug("TELEGRAM :: {} << 상품 알림 추가 메뉴에서 응답, 받은메세지 '{}'", telegramId, messageContents);

        sender().send(SendMessageVo.create(messageVo, CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final String productCode = danawaProductCodeCrawler.crawl(requestUrl);

        if (Objects.isNull(productCode)) {
            log.debug("APP :: {} << 링크에서 상품 코드를 찾을 수 없습니다. 받은메세지 '{}'", telegramId, messageContents);
            sender().send(SendMessageVo.create(messageVo, ProductAddUrlResponse.wrongProductUrl(requestUrl), createHomeKeyboard(telegramId), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        productAddHandler.handleProductAdd(messageVo, productCode);
    }
}
