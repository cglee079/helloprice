package com.podo.helloprice.telegram.client.menu.global;

import com.podo.helloprice.core.domain.item.CrawledItemVo;
import com.podo.helloprice.core.domain.model.Menu;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.TelegramMessageSender;
import com.podo.helloprice.telegram.client.menu.Keyboard;
import com.podo.helloprice.telegram.client.menu.itemadd.ItemAddResponse;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.global.cache.DanawaItemCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemAddHandler {

    private final DanawaItemCache danawaItemCache;
    private final UserService userService;
    private final ItemService itemService;
    private final TelegramMessageSender sender;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;

    public void handleItemAdd(TMessageVo tMessageVo, String itemCode) {
        final String telegramId = tMessageVo.getTelegramId();
        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        final CrawledItemVo crawledItem = danawaItemCache.get(itemCode);
        if (Objects.isNull(crawledItem)) {
            log.info("{} << 상품 정보를 가져 올 수 없습니다. 상품코드 '{}'", telegramId, itemCode);
            sender.send(tMessageVo.newMessage(ItemAddResponse.wrongItemCode(itemCode), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        if (!validateNewItem(tMessageVo, itemCommands, crawledItem)) {
            return;
        }

        final Long itemId = itemService.writeCrawledItem(crawledItem);

        final UserDto.detail userDetail = userService.findByTelegramId(telegramId);
        final ItemDto.detail itemDetail = itemService.findByItemId(itemId);

        if (userItemNotifyService.isExistedNotify(userDetail.getId(), itemDetail.getId())) {
            log.info("{} << {}({}) 상품 알림이 이미 등록되었습니다.", telegramId, crawledItem.getItemName(), itemCode);
            sender.send(tMessageVo.newMessage(CommonResponse.descItemDetail(itemDetail), itemDetail.getItemImage(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            sender.send(tMessageVo.newMessage(ItemAddResponse.alreadySetNotifyItem(), null, null, callbackFactory.createDefault(telegramId, null)));
            return;
        }

        log.info("{} << {}({}) 상품 알림을 등록합니다.", telegramId, crawledItem.getItemName(), itemCode);

        if (userService.hasMaxNotifyByUserTelegramId(telegramId)) {
            log.info("{} << 사용자는 이미 최대 상품알림 개수를 초과했습니다", telegramId);
            sender.send(tMessageVo.newMessage(ItemAddResponse.hasMaxItem(), null, Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        handleItemCommand(tMessageVo, telegramId, crawledItem, itemId, userDetail, itemDetail);
    }

    private void handleItemCommand(TMessageVo tMessageVo, String telegramId, CrawledItemVo crawledItemVo, Long itemId, UserDto.detail userDetail, ItemDto.detail itemDetail) {
        userItemNotifyService.addNewNotify(userDetail.getId(), itemId);

        final List<String> reloadItemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId)); // 갱신
        sender.send(tMessageVo.newMessage(CommonResponse.descItemDetail(itemDetail), crawledItemVo.getItemImage(), Keyboard.getHomeKeyboard(reloadItemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
        sender.send(tMessageVo.newMessage(ItemAddResponse.successAddNotifyItem(), null, null, callbackFactory.createDefault(telegramId, null)));
    }


    private boolean validateNewItem(TMessageVo tMessageVo, List<String> itemCommands, CrawledItemVo crawledItemVo) {
        final String telegramId = tMessageVo.getTelegramId();

        switch (crawledItemVo.getItemSaleStatus()) {
            case DISCONTINUE:
                log.info("{} << 추가요청한 {}({})는 단종된 상품 입니다", telegramId, crawledItemVo.getItemName(), crawledItemVo.getItemCode());
                sender.send(tMessageVo.newMessage(ItemAddResponse.isDiscontinueItem(crawledItemVo), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
                return false;
            case NOT_SUPPORT:
                log.info("{} << 추가요청한 {}({})는 가격비교중지 상품입니다", telegramId, crawledItemVo.getItemName(), crawledItemVo.getItemCode());
                sender.send(tMessageVo.newMessage(ItemAddResponse.isNotSupportItem(crawledItemVo), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
                return false;
            case UNKNOWN:
                log.info("{} << 추가요청한 상품 {}({})는 알 수 없는 상태의 상품입니다", telegramId, crawledItemVo.getItemName(), crawledItemVo.getItemCode());
                sender.send(tMessageVo.newMessage(ItemAddResponse.isErrorItem(crawledItemVo), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
                return false;
        }

        return true;
    }
}
