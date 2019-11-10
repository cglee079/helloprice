package com.podo.sadream.telegram.client.menu.home;

import com.podo.sadream.core.domain.item.ItemInfoVo;
import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.pooler.DanawaPooler;
import com.podo.sadream.telegram.client.response.CommonResponse;
import com.podo.sadream.telegram.client.KeyboardManager;
import com.podo.sadream.telegram.client.TMessageCallbackFactory;
import com.podo.sadream.telegram.client.TMessageVo;
import com.podo.sadream.telegram.client.menu.AbstractMenuHandler;
import com.podo.sadream.telegram.client.menu.itemadd.ItemAddResponse;
import com.podo.sadream.telegram.client.menu.itemdelete.ItemDeleteResponse;
import com.podo.sadream.telegram.domain.item.ItemDto;
import com.podo.sadream.telegram.domain.item.ItemService;
import com.podo.sadream.telegram.client.UserItemCommand;
import com.podo.sadream.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class HomeMenuHandler extends AbstractMenuHandler {

    @Value("${app.telegram.help_url}")
    private String helpUrl;

    private final ItemService itemService;
    private final UserItemNotifyService userItemNotifyService;
    private final DanawaPooler danawaPooler;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    @Override
    public Menu getHandleMenu() {
        return Menu.HOME;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId() + "";
        HomeCommand homeCommand = HomeCommand.from(requestMessage);

        log.info("{} << 홈메뉴에서 응답, 보낸메세지 '{}'", telegramId, requestMessage);

        List<String> itemCommands = UserItemCommand.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        if (Objects.isNull(homeCommand)) {

            //상품 명령어인지 체크,
            final String itemCode = UserItemCommand.getItemCodeFromCommand(requestMessage);

            // 잘못된 명령어
            if (Objects.isNull(itemCode)) {
                log.info("{} << 응답 할 수 없는 메세지 입니다 보낸메세지 '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.newValue(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                return;
            }

            //상품코드가 잘못된 경우
            if (itemService.existByItemCode(itemCode)) {
                log.info("{} << 잘못된 상품코드 메세지입니다. 보낸메세지 '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.newValue(HomeResponse.wrongItemCode(itemCode), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                return;
            }

            //정상 상품 명령어
            log.info("{} << 상품정보 요청을 확인했습니다. 보낸메세지 '{}'", telegramId, requestMessage);
            final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);
            final Long itemId = itemService.merge(itemInfoVo);
            final ItemDto.detail itemDetail = itemService.findByItemId(itemId);
            getBot().send(tMessageVo.newValue(HomeResponse.itemDetail(itemDetail), itemDetail.getItemImage(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));

            return;
        }


        switch (homeCommand) {
            case ADD_ITEM:
                log.info("{} << 상품 추가 메뉴로 이동. 보낸메세지 '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.newValue(ItemAddResponse.explain(DanawaPooler.DANAWA_URL, helpUrl), km.getDefaultKeyboard(), callbackFactory.createDefaultCallback(telegramId, Menu.ITEM_ADD)));
                break;
            case DELETE_ITEM:
                log.info("{} << 상품 삭제 메뉴로 이동. 보낸메세지 '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.newValue(ItemDeleteResponse.explain(), km.getItemDeleteKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.ITEM_DELETE)));
                break;
            case HELP:
                log.info("{} << 도움말. 보낸메세지 '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.newValue(CommonResponse.help(helpUrl), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                break;
        }

    }
}
