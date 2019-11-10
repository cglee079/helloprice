package com.podo.sadream.telegram.client.menu.itemadd;

import com.podo.sadream.core.domain.item.ItemInfoVo;
import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.pooler.DanawaPooler;
import com.podo.sadream.telegram.client.KeyboardManager;
import com.podo.sadream.telegram.client.TMessageCallbackFactory;
import com.podo.sadream.telegram.client.TMessageVo;
import com.podo.sadream.telegram.client.menu.AbstractMenuHandler;
import com.podo.sadream.telegram.domain.item.ItemDto;
import com.podo.sadream.telegram.domain.item.ItemService;
import com.podo.sadream.telegram.domain.user.UserDto;
import com.podo.sadream.telegram.domain.user.UserService;
import com.podo.sadream.telegram.domain.useritem.UserItemNotifyService;
import com.podo.sadream.telegram.client.UserItemCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemAddMenuHandler extends AbstractMenuHandler {

    private final ItemService itemService;
    private final UserService userService;
    private final UserItemNotifyService userItemNotifyService;

    private final DanawaPooler danawaPooler;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;


    @Override
    public Menu getHandleMenu() {
        return Menu.ITEM_ADD;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        log.info("{} << 상품 알림 추가 메뉴에서 응답, RequestMessage '{}'", telegramId, requestMessage);

        final String url = requestMessage;
        final String itemCode = danawaPooler.getItemCodeFromUrl(url);

        final List<String> itemCommands = UserItemCommand.getItemCommands(itemService.findByUserTelegramId(telegramId));

        //URL에서 아이템코드를 찾을 수 없음
        if (Objects.isNull(itemCode)) {
            log.info("{} << 링크에서 상품 코드를 찾을 수 없습니다. RequestMessage '{}'", telegramId, requestMessage);
            getBot().send(tMessageVo.create(ItemAddResponse.wrongItemUrl(url), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
            return;
        }

        final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);

        if (Objects.isNull(itemInfoVo)) {
            log.info("{} << 상품 정보를 가져 올 수 없습니다. RequestMessage '{}'", telegramId, requestMessage);
            getBot().send(tMessageVo.create(ItemAddResponse.wrongItem(url), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
            return;
        }

        if (!validateNewItem(tMessageVo, telegramId, itemCommands, itemInfoVo)) {
            return;
        }

        final UserDto.detail userDetail = userService.findByTelegramId(telegramId);
        final Long itemId = itemService.merge(itemInfoVo);
        final ItemDto.detail itemDetail = itemService.findByItemId(itemId);

        if (userItemNotifyService.hasNotify(userDetail.getId(), itemId)) {
            log.info("{} << {}({}) 상품 알림이 이미 등록되었습니다. RequestMessage '{}'", telegramId, itemInfoVo.getItemName(), itemCode, requestMessage);
            getBot().send(tMessageVo.create(ItemAddResponse.alreadySetNotifyItem(itemDetail), itemDetail.getItemImage(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
            return;
        }

        log.info("{} << {}({}) 상품 알림을 등록합니다. RequestMessage '{}'", telegramId, itemInfoVo.getItemName(), itemCode, requestMessage);
        userItemNotifyService.addNotify(userDetail.getId(), itemId);
        List<String> reloadItemCommands = UserItemCommand.getItemCommands(itemService.findByUserTelegramId(telegramId)); // 갱신
        getBot().send(tMessageVo.create(ItemAddResponse.successAddNotifyItem(itemDetail), itemInfoVo.getItemImage(), km.getHomeKeyboard(reloadItemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
    }

    private boolean validateNewItem(TMessageVo tMessageVo, String telegramId, List<String> itemCommands, ItemInfoVo itemInfoVo) {
        switch (itemInfoVo.getItemSaleStatus()) {
            case DISCONTINUE:
                log.info("{} << 추가요청한 {}({})는 단종된 상품 입니다", telegramId, itemInfoVo.getItemName(), itemInfoVo.getItemCode());
                getBot().send(tMessageVo.create(ItemAddResponse.isDiscontinueItem(itemInfoVo), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                return false;
            case NOT_SUPPORT:
                log.info("{} << 추가요청한 {}({})는 가격비교중지 상품입니다", telegramId, itemInfoVo.getItemName(), itemInfoVo.getItemCode());
                getBot().send(tMessageVo.create(ItemAddResponse.isNotSupportItem(itemInfoVo), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                return false;
            case ERROR:
                log.info("{} << 추가요청한 상품 {}({})는 알 수 없는 상태의 상품입니다", telegramId, itemInfoVo.getItemName(), itemInfoVo.getItemCode());
                getBot().send(tMessageVo.create(ItemAddResponse.isErrorItem(itemInfoVo), km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
                return false;
        }

        return true;
    }
}
