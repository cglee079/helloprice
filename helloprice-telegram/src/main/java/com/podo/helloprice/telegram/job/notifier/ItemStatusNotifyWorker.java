package com.podo.helloprice.telegram.job.notifier;

import com.podo.helloprice.core.domain.item.ItemStatus;
import com.podo.helloprice.core.domain.item.ItemUpdateStatus;
import com.podo.helloprice.core.domain.user.UserStatus;
import com.podo.helloprice.core.util.MyCalculateUtils;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.job.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemStatusNotifyWorker implements Worker {

    @Value("${item.max_dead_count}")
    private Integer maxDeadCount;

    @Value("${item.max_unknown_count}")
    private Integer maxUnknownCount;

    private int deadCount = 0;
    private int unknownCount = 0;

    private final GlobalNotifier globalNotifier;
    private final ItemService itemService;
    private final UserItemNotifyService userItemNotifyService;

    @Override
    public void doIt() {
        log.info("상품 업데이트 알림 WORKER, 상품 상태체크를 시작합니다");

        final LocalDateTime now = LocalDateTime.now();

        handleItemStatusUpdated(now);
        handleItemStatusDead();
    }

    private void handleItemStatusUpdated(LocalDateTime notifyAt) {
        final List<ItemDto.detail> items = new ArrayList<>();

        items.addAll(itemService.findByItemStatusAndItemUpdateStatus(ItemStatus.ALIVE, ItemUpdateStatus.UPDATED));
        items.addAll(itemService.findByItemStatusAndItemUpdateStatus(ItemStatus.PAUSE, ItemUpdateStatus.UPDATED));

        for (ItemDto.detail item : items) {
            switch (item.getItemSaleStatus()) {
                case DISCONTINUE:
                    handleDiscontinueItem(item);
                    break;
                case UNKNOWN:
                    handleUnknownItem(item);
                    break;
                case EMPTY_AMOUNT:
                    handleEmptyAmount(item, notifyAt);
                    break;
                case NOT_SUPPORT:
                    handleNotSupport(item);
                    break;
                case SALE:
                    handleSale(item, notifyAt);
                    break;
            }

            itemService.notifiedItem(item.getId());
        }

    }

    private List<NotifyUserVo> getNotifyUsersByItemId(Long itemId) {
        final List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemId(itemId, UserStatus.ALIVE);
        return users.stream()
                .map(user -> new NotifyUserVo(user.getUsername(), user.getEmail(), user.getTelegramId()))
                .collect(toList());
    }

    private void handleSale(ItemDto.detail item, LocalDateTime notifyAt) {
        log.info("{}({}) 상품의 최저가가 갱신되었습니다.", item.getItemName(), item.getItemCode());

        final double changePercent = MyCalculateUtils.getChangePercent(item.getItemPrice(), item.getItemBeforePrice());

        if ((Math.abs(changePercent) > 1) && (changePercent < 0)) {
            globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), NotifyTitle.notifyItemSale(item), item.getItemImage(), NotifyContents.notifyItemSale(item));
            userItemNotifyService.updateNotifyAt(item.getId(), notifyAt);
            return;
        }

        log.info("{}({}) 상품의 가격변화율('{}%')이 적합하지 않아 알림을 전송하지 않습니다", item.getItemName(), item.getItemCode(), new DecimalFormat("#.##").format(changePercent));
    }


    private void handleEmptyAmount(ItemDto.detail item, LocalDateTime notifyAt) {
        log.info("{}({}) 상품은 재고없음 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());
        globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), NotifyTitle.notifyItemEmptyAccount(item), item.getItemImage(), NotifyContents.notifyItemEmptyAccount(item));
        userItemNotifyService.updateNotifyAt(item.getId(), notifyAt);
    }


    private void handleNotSupport(ItemDto.detail item) {
        log.info("{}({}) 상품은 가격격비교중지 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), NotifyTitle.notifyItemNotSupport(item), item.getItemImage(), NotifyContents.notifyItemNotSupport(item));

        userItemNotifyService.deleteNotifies(item.getId());
    }

    private void handleUnknownItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 알 수 없는 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        increaseUnknownCount();

        globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), NotifyTitle.notifyItemUnknown(item), item.getItemImage(), NotifyContents.notifyItemUnknown(item));

        userItemNotifyService.deleteNotifies(item.getId());
    }

    private void handleDiscontinueItem(ItemDto.detail item) {
        log.info("{}({}) 상품은 단종 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), NotifyTitle.notifyItemDiscontinued(item), item.getItemImage(), NotifyContents.notifyItemDiscontinued(item));

        userItemNotifyService.deleteNotifies(item.getId());
    }

    private void increaseUnknownCount() {
        this.unknownCount++;

        if (unknownCount >= maxUnknownCount) {
            log.info("{} 이상 상품 상태를 확인 할 수 없습니다", unknownCount);
            globalNotifier.notifyAdmin(NotifyTitle.notifyTooManyUnknown(unknownCount), NotifyContents.notifyTooManyUnknown(unknownCount));

            unknownCount = 0;
        }
    }

    private void handleItemStatusDead() {
        List<ItemDto.detail> items = itemService.findByItemStatusAndItemUpdateStatus(ItemStatus.DEAD, ItemUpdateStatus.UPDATED);

        for (ItemDto.detail item : items) {
            increaseDeadCount();

            log.info("{}({}) 상품은 페이지를 확인 할 수 없는 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

            globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), NotifyTitle.notifyItemDead(item), item.getItemImage(), NotifyContents.notifyItemDead(item));

            userItemNotifyService.deleteNotifies(item.getId());

            itemService.notifiedItem(item.getId());
        }
    }

    private void increaseDeadCount() {
        this.deadCount++;

        if (deadCount >= maxDeadCount) {
            log.info("{} 이상 상품페이지를 확인 할 수 없습니다", deadCount);
            globalNotifier.notifyAdmin(NotifyTitle.notifyTooManyDead(unknownCount), NotifyContents.notifyTooManyDead(unknownCount));
            deadCount = 0;
        }

    }


}
