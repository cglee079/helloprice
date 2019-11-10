package com.podo.helloprice.telegram.job;

import com.podo.helloprice.core.domain.item.ItemStatus;
import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.core.domain.user.UserStatus;
import com.podo.helloprice.core.util.MyCalculateUtils;
import com.podo.helloprice.telegram.client.*;
import com.podo.helloprice.telegram.client.response.NotifyResponse;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemStatusCheckWorker implements Worker {

    @Value("${telegram.podo_helloprice.admin.id}")
    private Integer adminTelegramId;

    @Value("${item.max_dead_count}")
    private Integer maxDeadCount;

    @Value("${item.max_error_count}")
    private Integer maxErrorCount;

    private int deadCount = 0;
    private int errorCount = 0;

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
        log.info("{}({}) 상품의 최저가가 갱신되었습니다.", item.getItemName(), item.getItemCode());

        final double changePercent = MyCalculateUtils.getChangePercent(item.getItemPrice(), item.getItemBeforePrice());
        if (Math.abs(changePercent) < 1) {
            log.info("{}({}) 상품의 가격변화율('{}%')이 너무 작아 알림을 전송하지 않습니다", item.getItemName(), item.getItemCode(), new DecimalFormat("#.##").format(changePercent));
            itemService.notifiedItem(item.getId());
            return;
        }


        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemId(item.getId(), UserStatus.ALIVE);

        for (UserDto.detail user : users) {
            notifyUser(user.getTelegramId(), item.getItemImage(), NotifyResponse.notifyItemSale(item));
        }

        itemService.notifiedItem(item.getId());
    }

    private void handleEmptyAmount(ItemDto.detail item) {
        log.info("{}({}) 상품은 재고없음 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemId(item.getId(), UserStatus.ALIVE);

        for (UserDto.detail user : users) {
            notifyUser(user.getTelegramId(), item.getItemImage(), NotifyResponse.notifyItemEmptyAccount(item));
        }

        itemService.notifiedItem(item.getId());
    }

    private void handleErrorItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 알 수 없는 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemId(item.getId(), UserStatus.ALIVE);

        for (UserDto.detail user : users) {
            userItemNotifyService.deleteNotify(user.getId(), item.getId());

            notifyUser(user.getTelegramId(), item.getItemImage(), NotifyResponse.notifyItemError(item));
        }

        itemService.deleteByItemId(item.getId());

    }

    private void handleDiscontinueItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 단종 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        increaseErrorCount();
        List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemId(item.getId(), UserStatus.ALIVE);

        for (UserDto.detail user : users) {
            userItemNotifyService.deleteNotify(user.getId(), item.getId());

            notifyUser(user.getTelegramId(), item.getItemImage(), NotifyResponse.notifyItemDiscontinued(item));
        }

        itemService.deleteByItemId(item.getId());
    }

    private void increaseErrorCount() {
        this.errorCount++;

        if (errorCount >= maxErrorCount) {
            log.info("{} 이상 상품 상태를 확인 할 수 없습니다", deadCount);
            notifyUser(adminTelegramId, null, NotifyResponse.notifyTooManyDead(errorCount));
            errorCount = 0;
        }

    }

    private void handleItemStatusDead() {
        List<ItemDto.detail> items = itemService.findByItemStatus(ItemStatus.DEAD);

        for (ItemDto.detail item : items) {
            increaseDeadCount();
            log.info("{}({}) 상품은 페이지를 확인 할 수 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

            List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemId(item.getId(), UserStatus.ALIVE);

            for (UserDto.detail user : users) {
                userItemNotifyService.deleteNotify(user.getId(), item.getId());

                notifyUser(user.getTelegramId(), null, NotifyResponse.notifyItemDead(item));
            }

            itemService.deleteByItemId(item.getId());
        }
    }

    private void increaseDeadCount() {
        this.deadCount++;

        if (deadCount >= maxDeadCount) {
            log.info("{} 이상 상품페이지를 확인 할 수 없습니다", deadCount);
            notifyUser(adminTelegramId, null, NotifyResponse.notifyTooManyError(deadCount));
            deadCount = 0;
        }

    }

    private void notifyUser(Integer telegramId, String itemImage, String response) {
        log.info("{}님에게 변경상태 알림을 전송합니다", telegramId);

        TMessageVo tMessageVo = new TMessageVo(telegramId, null);

        List<String> itemCommands = UserItemCommand.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId + ""));
        telegramBot.send(tMessageVo.newValue(response, itemImage, km.getHomeKeyboard(itemCommands), callbackFactory.createDefaultCallback(telegramId + "", Menu.HOME)));
    }
}
