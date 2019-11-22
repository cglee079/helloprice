package com.podo.helloprice.telegram.client.menu.itemadd;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.crawler.target.danawa.DanawaCrawler;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.global.ItemAddHandler;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemAddMenuHandler extends AbstractMenuHandler {

    private final UserItemNotifyService userItemNotifyService;

    private final DanawaCrawler danawaCrawler;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;
    private final ItemAddHandler itemAddHandler;


    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_ADD;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId();

        log.info("{} << 상품 알림 추가 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        sender().send(tMessageVo.newMessage(CommonResponse.justWait(), null, callbackFactory.createDefaultNoAction(telegramId)));

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        final String url = requestMessage;
        final String itemCodeFromUrl = danawaCrawler.getItemCodeFromUrl(url);

        if (Objects.isNull(itemCodeFromUrl)) {
            log.info("{} << 링크에서 상품 코드를 찾을 수 없습니다. 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(tMessageVo.newMessage(ItemAddResponse.wrongItemUrl(url), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        itemAddHandler.handleItemAdd(tMessageVo, itemCodeFromUrl);

    }

}
