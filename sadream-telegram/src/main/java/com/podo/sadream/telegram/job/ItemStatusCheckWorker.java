package com.podo.sadream.telegram.job;

import com.podo.sadream.core.domain.item.ItemStatus;
import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.telegram.client.*;
import com.podo.sadream.telegram.client.response.NotifyResponse;
import com.podo.sadream.telegram.domain.item.ItemDto;
import com.podo.sadream.telegram.domain.item.ItemService;
import com.podo.sadream.telegram.domain.user.UserDto;
import com.podo.sadream.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemStatusCheckWorker implements Worker {

    private final ItemService itemService;
    private final UserItemNotifyService userItemNotifyService;
    private final KeyboardManager km;
    private final TelegramBot telegramBot;
    private final TMessageCallbackFactory callbackFactory;

    @Override
    public void doIt() {
        log.info("상품 상태체크를 시작합니다");

        handleItemStatusUpdated();
        handleItemStatusDead();
    }


    private void handleItemStatusUpdated() {
        List<ItemDto.detail> items = itemService.findByItemStatus(ItemStatus.UPDATED);

        for (ItemDto.detail item : items) {
            switch (item.getItemSaleStatus()) {
                case DISCONTINUE:
                    handleDiscontinueItem(item);
                    break;
                case ERROR:
                    handleErrorItem(item);
                    break;
                case EMPTY_AMOUNT:
                    handleEmptyAmount(item);
                    break;
                case SALE:
                    handleSale(item);
                    break;
            }

        }
    }

    private void handleSale(ItemDto.detail item) {
        log.info("{}({}) 상품은 판매중 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItem(item.getId());

        for (UserDto.detail user : users) {
            notifyUser(user, item.getItemImage(), NotifyResponse.notifyItemSale(item));
        }

        itemService.notifiedUpdate(item.getId());
    }

    private void handleEmptyAmount(ItemDto.detail item) {
        log.info("{}({}) 상품은 재고없음 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItem(item.getId());

        for (UserDto.detail user : users) {
            notifyUser(user, item.getItemImage(), NotifyResponse.notifyItemEmptyAccount(item));
        }

        itemService.notifiedUpdate(item.getId());
    }

    private void handleErrorItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 알 수 없는 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItem(item.getId());

        for (UserDto.detail user : users) {
            userItemNotifyService.deleteNotify(user.getId(), item.getId());

            notifyUser(user, item.getItemImage(), NotifyResponse.notifyItemError(item));
        }

        itemService.deleteByItemId(item.getId());

    }

    private void handleDiscontinueItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 단종 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItem(item.getId());

        for (UserDto.detail user : users) {
            userItemNotifyService.deleteNotify(user.getId(), item.getId());

            notifyUser(user, item.getItemImage(), NotifyResponse.notifyItemDiscontinued(item));
        }

        itemService.deleteByItemId(item.getId());
    }

    private void handleItemStatusDead() {
        List<ItemDto.detail> items = itemService.findByItemStatus(ItemStatus.DEAD);

        for (ItemDto.detail item : items) {
            log.info("{}({}) 상품은 페이지를 확인 할 수 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

            List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItem(item.getId());

            for (UserDto.detail user : users) {
                userItemNotifyService.deleteNotify(user.getId(), item.getId());

                notifyUser(user, null, NotifyResponse.notifyItemDead(item));
            }

            itemService.deleteByItemId(item.getId());
        }
    }

    private void notifyUser(UserDto.detail user, String itemImage, String response) {
        log.info("{}님에게 변경상태 알림을 전송합니다", user.getTelegramId());

        String telegramId = user.getTelegramId() + "";
        TMessageVo tMessageVo = new TMessageVo(user.getTelegramId(), null);

        List<String> itemCommands = UserItemCommand.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));
        telegramBot.send(tMessageVo.newValue(response, itemImage, km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId, Menu.HOME)));
    }
}
