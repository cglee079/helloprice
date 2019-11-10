package com.podo.itemwatcher.telegram.client.menu.home;

import com.podo.itemwatcher.core.domain.item.ItemInfoVo;
import com.podo.itemwatcher.core.domain.user.Menu;
import com.podo.itemwatcher.pooler.DanawaPooler;
import com.podo.itemwatcher.telegram.client.CommonResponse;
import com.podo.itemwatcher.telegram.client.KeyboardManager;
import com.podo.itemwatcher.telegram.client.TMessageCallbackFactory;
import com.podo.itemwatcher.telegram.client.TMessageVo;
import com.podo.itemwatcher.telegram.client.menu.AbstractMenuHandler;
import com.podo.itemwatcher.telegram.client.menu.itemadd.ItemAddResponse;
import com.podo.itemwatcher.telegram.client.menu.itemdelete.ItemDeleteResponse;
import com.podo.itemwatcher.telegram.domain.item.ItemDto;
import com.podo.itemwatcher.telegram.domain.item.ItemService;
import com.podo.itemwatcher.telegram.client.UserItemCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class HomeMenuHandler extends AbstractMenuHandler {

    private final ItemService itemService;
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

        log.info("{} << 홈메뉴에서 응답, RequestMessage '{}'", telegramId, requestMessage);

        List<String> itemCommands = UserItemCommand.getItemCommands(itemService.findByUserTelegramId(telegramId));

        if (Objects.isNull(homeCommand)) {

            //상품 명령어인지 체크,
            final String itemCode = UserItemCommand.getItemCodeFromCommand(requestMessage);

            // 잘못된 명령어
            if (Objects.isNull(itemCode)) {
                log.info("{} << 응답 할 수 없는 메세지 입니다 RequestMessage '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.create(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                return;
            }

            //상품코드가 잘못된 경우
            if (itemService.existByItemCode(itemCode)) {
                log.info("{} << 잘못된 상품코드 메세지입니다. RequestMessage '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.create(HomeResponse.wrongItemCode(itemCode), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                return;
            }

            //정상 상품 명령어
            log.info("{} << 상품정보 요청을 확인했습니다. RequestMessage '{}'", telegramId, requestMessage);
            final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);
            final Long itemId = itemService.merge(itemInfoVo);
            final ItemDto.detail itemDetail = itemService.findByItemId(itemId);
            getBot().send(tMessageVo.create(HomeResponse.itemDetail(itemDetail), itemDetail.getItemImage(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));

            return;
        }


        switch (homeCommand) {
            case ADD_ITEM:
                log.info("{} << 상품 추가 메뉴로 이동. RequestMessage '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.create(ItemAddResponse.explain(DanawaPooler.DANAWA_URL), km.getDefaultKeyboard(), callbackFactory.createDefaultCallback(telegramId, Menu.ITEM_ADD)));
                break;
            case DELETE_ITEM:
                log.info("{} << 상품 삭제 메뉴로 이동. RequestMessage '{}'", telegramId, requestMessage);
                getBot().send(tMessageVo.create(ItemDeleteResponse.explain(), km.getItemDeleteKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.ITEM_DELETE)));
                break;
        }

    }
}
