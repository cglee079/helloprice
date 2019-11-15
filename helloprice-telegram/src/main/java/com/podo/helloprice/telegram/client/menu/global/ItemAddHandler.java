package com.podo.helloprice.telegram.client.menu.global;

import com.podo.helloprice.core.domain.item.ItemInfoVo;
import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.pooler.target.danawa.DanawaPooler;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.TelegramMessageSender;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.menu.itemadd.ItemAddResponse;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemAddHandler {

    private final DanawaPooler danawaPooler;
    private final UserService userService;
    private final ItemService itemService;
    private final TelegramMessageSender sender;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    public void handleItemAdd(TMessageVo tMessageVo, String itemCode) {
        final String telegramId = tMessageVo.getTelegramId() + "";
        final ItemInfoVo itemInfoVo = danawaPooler.poolItem(itemCode);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        if (Objects.isNull(itemInfoVo)) {
            log.info("{} << 상품 정보를 가져 올 수 없습니다. 상품코드 '{}'", telegramId, itemCode);
            sender.send(tMessageVo.newValue(ItemAddResponse.wrongItemCode(itemCode), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        if (!validateNewItem(tMessageVo, itemCommands, itemInfoVo)) {
            return;
        }

        final Long itemId = itemService.merge(itemInfoVo);

        final UserDto.detail userDetail = userService.findByTelegramId(telegramId);
        final ItemDto.detail itemDetail = itemService.findByItemId(itemId);

        if (userItemNotifyService.hasNotify(userDetail.getId(), itemDetail.getId())) {
            log.info("{} << {}({}) 상품 알림이 이미 등록되었습니다.", telegramId, itemInfoVo.getItemName(), itemCode);
            sender.send(tMessageVo.newValue(CommonResponse.descItemDetail(itemDetail), itemDetail.getItemImage(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            sender.send(tMessageVo.newValue(ItemAddResponse.alreadySetNotifyItem(), null, null, callbackFactory.createDefault(telegramId, null)));
            return;
        }

        log.info("{} << {}({}) 상품 알림을 등록합니다.", telegramId, itemInfoVo.getItemName(), itemCode);

        if (userItemNotifyService.hasMaxNotifyByUserTelegramId(telegramId)) {
            log.info("{} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender.send(tMessageVo.newValue(ItemAddResponse.hasMaxItem(), null, km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        handleItemCommand(tMessageVo, telegramId, itemInfoVo, itemId, userDetail, itemDetail);
    }

    private void handleItemCommand(TMessageVo tMessageVo, String telegramId, ItemInfoVo itemInfoVo, Long itemId, UserDto.detail userDetail, ItemDto.detail itemDetail) {
        userItemNotifyService.addNotify(userDetail.getId(), itemId);

        final List<String> reloadItemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId)); // 갱신
        sender.send(tMessageVo.newValue(CommonResponse.descItemDetail(itemDetail), itemInfoVo.getItemImage(), km.getHomeKeyboard(reloadItemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
        sender.send(tMessageVo.newValue(ItemAddResponse.successAddNotifyItem(), null, null, callbackFactory.createDefault(telegramId, null)));
    }


    private boolean validateNewItem(TMessageVo tMessageVo, List<String> itemCommands, ItemInfoVo itemInfoVo) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        switch (itemInfoVo.getItemSaleStatus()) {
            case DISCONTINUE:
                log.info("{} << 추가요청한 {}({})는 단종된 상품 입니다", telegramId, itemInfoVo.getItemName(), itemInfoVo.getItemCode());
                sender.send(tMessageVo.newValue(ItemAddResponse.isDiscontinueItem(itemInfoVo), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
                return false;
            case NOT_SUPPORT:
                log.info("{} << 추가요청한 {}({})는 가격비교중지 상품입니다", telegramId, itemInfoVo.getItemName(), itemInfoVo.getItemCode());
                sender.send(tMessageVo.newValue(ItemAddResponse.isNotSupportItem(itemInfoVo), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
                return false;
            case UNKNOWN:
                log.info("{} << 추가요청한 상품 {}({})는 알 수 없는 상태의 상품입니다", telegramId, itemInfoVo.getItemName(), itemInfoVo.getItemCode());
                sender.send(tMessageVo.newValue(ItemAddResponse.isErrorItem(itemInfoVo), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
                return false;
        }

        return true;
    }
}
