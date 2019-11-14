package com.podo.helloprice.telegram.client.menu.home;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.pooler.target.danawa.DanawaPoolConfig;
import com.podo.helloprice.telegram.client.menu.itemsearch.ItemSearchResponse;
import com.podo.helloprice.telegram.client.response.CommonResponse;
import com.podo.helloprice.telegram.client.KeyboardManager;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.itemadd.ItemAddResponse;
import com.podo.helloprice.telegram.client.menu.itemdelete.ItemDeleteResponse;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.client.UserItemCommand;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
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

    @Value("${app.name}")
    private String appName;

    @Value("${app.telegram.help_url}")
    private String helpUrl;

    private final ItemService itemService;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    @Override
    public Menu getHandleMenu() {
        return Menu.HOME;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        log.info("{} << 홈메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = UserItemCommand.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        final HomeCommand requestCommand = HomeCommand.from(requestMessage);

        if (Objects.nonNull(requestCommand)) {
            handleCommand(tMessageVo, requestMessage, telegramId, requestCommand, itemCommands);
            return;
        }


        //상품 명령어인지 체크,
        final String itemCode = UserItemCommand.getItemCodeFromCommand(requestMessage);

        // 잘못된 명령어
        if (Objects.isNull(itemCode)) {
            log.info("{} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, requestMessage);
            getSender().send(tMessageVo.newValue(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        //상품코드가 잘못된 경우
        if (itemService.existByItemCode(itemCode)) {
            log.info("{} << 잘못된 상품코드 메세지입니다. 받은메세지 '{}'", telegramId, requestMessage);
            getSender().send(tMessageVo.newValue(HomeResponse.wrongItemCode(itemCode), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        //정상 상품 명령어
        log.info("{} << 상품정보 요청을 확인했습니다. 받은메세지 '{}'", telegramId, requestMessage);
        handleItemCommands(tMessageVo, telegramId, itemCode);
    }

    private void handleItemCommands(TMessageVo tMessageVo, String telegramId, String itemCode) {
//        final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);
//        final Long itemId = itemService.merge(itemInfoVo);
        final ItemDto.detail itemDetail = itemService.findByItemCode(itemCode);
        getSender().send(tMessageVo.newValue(HomeResponse.itemDetail(itemDetail), itemDetail.getItemImage(), null, callbackFactory.createDefault(telegramId, Menu.HOME)));
    }

    private void handleCommand(TMessageVo tMessageVo, String requestMessage, String telegramId, HomeCommand requestCommand, List<String> itemCommands) {
        switch (requestCommand) {
            case ITEM_SEARCH_ADD:
                log.info("{} << 상품 검색 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                getSender().send(tMessageVo.newValue(ItemSearchResponse.explain(), km.getDefaultKeyboard(), callbackFactory.createDefault(telegramId, Menu.ITEM_SEARCH)));
                break;

            case ITEM_ADD:
                log.info("{} << 상품 추가 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                getSender().send(tMessageVo.newValue(ItemAddResponse.explain(DanawaPoolConfig.Global.DANAWA_URL), km.getDefaultKeyboard(), callbackFactory.createDefault(telegramId, Menu.ITEM_ADD)));
                getSender().send(tMessageVo.newValue(CommonResponse.help(helpUrl), null, callbackFactory.createDefault(telegramId, null)));
                break;
            case ITEM_DELETE:
                log.info("{} << 상품 삭제 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                getSender().send(tMessageVo.newValue(ItemDeleteResponse.explain(), km.getItemDeleteKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.ITEM_DELETE)));
                break;
            case HELP:
                log.info("{} << 도움말. 받은메세지 '{}'", telegramId, requestMessage);
                getSender().send(tMessageVo.newValue(CommonResponse.introduce(appName), null, callbackFactory.createDefault(telegramId, Menu.HOME)));
                getSender().send(tMessageVo.newValue(CommonResponse.help(helpUrl), null, callbackFactory.createDefault(telegramId, null)));
                break;
        }
    }
}
