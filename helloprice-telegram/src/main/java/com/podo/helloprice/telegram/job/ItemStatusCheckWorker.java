package com.podo.helloprice.telegram.job;

import com.podo.helloprice.core.domain.item.ItemStatus;
import com.podo.helloprice.core.domain.item.ItemUpdateStatus;
import com.podo.helloprice.core.util.MyCalculateUtils;
import com.podo.helloprice.telegram.client.*;
import com.podo.helloprice.telegram.client.response.NotifyResponse;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
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

    @Value("${item.max_dead_count}")
    private Integer maxDeadCount;

    @Value("${item.max_unknown_count}")
    private Integer maxUnknownCount;

    private int deadCount = 0;
    private int unknownCount = 0;

    private final ItemService itemService;
    private final TelegramNotifier telegramNotifier;
    private final UserItemNotifyService userItemNotifyService;

    @Override
    public void doIt() {
        log.info("상품 상태체크를 시작합니다");

        handleItemStatusUpdated();
        handleItemStatusDead();
    }

    private void handleItemStatusUpdated() {
        List<ItemDto.detail> items = itemService.findByItemUpdateStatus(ItemUpdateStatus.UPDATED);

        for (ItemDto.detail item : items) {
            switch (item.getItemSaleStatus()) {
                case DISCONTINUE:
                    handleDiscontinueItem(item);
                    break;
                case UNKNOWN:
                    handleUnknownItem(item);
                    break;
                case EMPTY_AMOUNT:
                    handleEmptyAmount(item);
                    break;
                case NOT_SUPPORT:
                    handleNotSupport(item);
                    break;
                case SALE:
                    handleSale(item);
                    break;
            }

            itemService.notifiedItem(item.getId());
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

        telegramNotifier.notifyUsers(item.getId(), item.getItemImage(), NotifyResponse.notifyItemSale(item));

    }

    private void handleEmptyAmount(ItemDto.detail item) {
        log.info("{}({}) 상품은 재고없음 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        telegramNotifier.notifyUsers(item.getId(), item.getItemImage(), NotifyResponse.notifyItemEmptyAccount(item));
    }

    private void handleNotSupport(ItemDto.detail item) {
        log.info("{}({}) 상품은 가격격비교중지 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        telegramNotifier.notifyUsers(item.getId(), item.getItemImage(), NotifyResponse.notifyItemNotSupprt(item));
        userItemNotifyService.deleteNotifies(item.getId());
    }

    private void handleUnknownItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 알 수 없는 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        telegramNotifier.notifyUsers(item.getId(), item.getItemImage(), NotifyResponse.notifyItemUnknown(item));
        userItemNotifyService.deleteNotifies(item.getId());
    }

    private void handleDiscontinueItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 단종 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        increaseErrorCount();

        telegramNotifier.notifyUsers(item.getId(), item.getItemImage(), NotifyResponse.notifyItemDiscontinued(item));
        userItemNotifyService.deleteNotifies(item.getId());
    }

    private void increaseErrorCount() {
        this.unknownCount++;

        if (unknownCount >= maxUnknownCount) {
            log.info("{} 이상 상품 상태를 확인 할 수 없습니다", unknownCount);
            telegramNotifier.notifyAdmin(NotifyResponse.notifyTooManyUnknown(unknownCount));
            unknownCount = 0;
        }
    }

    private void handleItemStatusDead() {
        List<ItemDto.detail> items = itemService.findByItemStatus(ItemStatus.DEAD);

        for (ItemDto.detail item : items) {
            increaseDeadCount();

            log.info("{}({}) 상품은 페이지를 확인 할 수 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

            telegramNotifier.notifyUsers(item.getId(), item.getItemImage(), NotifyResponse.notifyItemDead(item));
            userItemNotifyService.deleteNotifies(item.getId());

            itemService.notifiedItem(item.getId());
        }
    }

    private void increaseDeadCount() {
        this.deadCount++;

        if (deadCount >= maxDeadCount) {
            log.info("{} 이상 상품페이지를 확인 할 수 없습니다", deadCount);
            telegramNotifier.notifyAdmin(NotifyResponse.notifyTooManyDead(deadCount));
            deadCount = 0;
        }

    }


}
