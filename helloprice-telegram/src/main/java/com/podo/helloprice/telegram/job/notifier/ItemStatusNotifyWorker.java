package com.podo.helloprice.telegram.job.notifier;

import com.podo.helloprice.core.domain.item.model.ItemStatus;
import com.podo.helloprice.core.domain.item.model.ItemUpdateStatus;
import com.podo.helloprice.core.domain.user.model.UserStatus;
import com.podo.helloprice.core.util.CalculateUtil;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.domain.notifylog.NotifyLogDto;
import com.podo.helloprice.telegram.domain.notifylog.NotifyLogService;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.job.Worker;
import com.podo.helloprice.telegram.job.notifier.message.NotifyContents;
import com.podo.helloprice.telegram.job.notifier.message.NotifyTitle;
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
    private final NotifyLogService notifyLogService;
    private final UserItemNotifyService userItemNotifyService;

    @Override
    public void doIt() {
        log.info("상품 업데이트 알림 WORKER, 상품 상태체크를 시작합니다");

        final LocalDateTime now = LocalDateTime.now();

        handleItemUpdateStatusUpdated(now);
        handleItemStatusDead();
    }

    private void handleItemUpdateStatusUpdated(LocalDateTime notifyAt) {
        final List<ItemDto.detail> items = new ArrayList<>();

        items.addAll(itemService.findByStatusAndUpdateStatus(ItemStatus.ALIVE, ItemUpdateStatus.UPDATED));
        items.addAll(itemService.findByStatusAndUpdateStatus(ItemStatus.PAUSE, ItemUpdateStatus.UPDATED));

        for (ItemDto.detail item : items) {
            handleByItemSaleStatus(notifyAt, item);
            itemService.notifiedItem(item.getId());
        }

    }

    private void handleByItemSaleStatus(LocalDateTime notifyAt, ItemDto.detail item) {
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
    }

    private void handleSale(ItemDto.detail item, LocalDateTime notifyAt) {

        if (item.getItemBeforePrice() == 0) {
            handleResale(item, notifyAt);
            return;
        }

        log.info("{}({}) 상품의 최저가가 갱신되었습니다.", item.getItemName(), item.getItemCode());
        final double changePercent = CalculateUtil.getChangePercent(item.getItemPrice(), item.getItemBeforePrice());

        if ((Math.abs(changePercent) > 1) && (changePercent < 0)) {
            logging(item);

            if (changePercent < -30) { //이스터에그
                globalNotifier.notifyAdmin(NotifyTitle.notifyItemSale(item), item.getItemImage(), NotifyContents.notifyItemSale(item));
            }

            notify(item, NotifyTitle.notifyItemSale(item), NotifyContents.notifyItemSale(item), notifyAt);
            return;
        }

        log.info("{}({}) 상품의 가격변화율('{}%')이 적합하지 않아 알림을 전송하지 않습니다", item.getItemName(), item.getItemCode(), new DecimalFormat("#.##").format(changePercent));
    }

    private void handleResale(ItemDto.detail item, LocalDateTime notifyAt) {
        logging(item);
        log.info("{}({}) 상품은 이전가가 0원이고, 다시 판매를 시작했습니다", item.getItemName(), item.getItemCode());
        notify(item, NotifyTitle.notifyItemReSale(item), NotifyContents.notifyItemResale(item), notifyAt);
    }


    private void handleEmptyAmount(ItemDto.detail item, LocalDateTime notifyAt) {
        logging(item);
        log.info("{}({}) 상품은 재고없음 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());
        notify(item, NotifyTitle.notifyItemEmptyAccount(item), NotifyContents.notifyItemEmptyAccount(item), notifyAt);
    }

    private void handleNotSupport(ItemDto.detail item) {
        logging(item);
        log.info("{}({}) 상품은 가격격비교중지 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());
        notifyAndRemoveNotify(item, NotifyTitle.notifyItemNotSupport(item), NotifyContents.notifyItemNotSupport(item));
    }

    private void handleUnknownItem(ItemDto.detail item) {
        logging(item);
        log.info("{}({}) 상품은 알 수 없는 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());
        increaseUnknownCount();
        notifyAndRemoveNotify(item, NotifyTitle.notifyItemUnknown(item), NotifyContents.notifyItemUnknown(item));
    }

    private void handleDiscontinueItem(ItemDto.detail item) {
        logging(item);
        log.info("{}({}) 상품은 단종 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

        notifyAndRemoveNotify(item, NotifyTitle.notifyItemDiscontinued(item), NotifyContents.notifyItemDiscontinued(item));
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
        List<ItemDto.detail> items = itemService.findByStatusAndUpdateStatus(ItemStatus.DEAD, ItemUpdateStatus.UPDATED);

        for (ItemDto.detail item : items) {
            logging(item);

            increaseDeadCount();

            log.info("{}({}) 상품은 페이지를 확인 할 수 없는 상태로 변경되었습니다.", item.getItemName(), item.getItemCode());

            notifyAndRemoveNotify(item, NotifyTitle.notifyItemDead(item), NotifyContents.notifyItemDead(item));

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

    private void notify(ItemDto.detail item, String title, String contents, LocalDateTime notifiedAt) {
        globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), title, item.getItemImage(), contents);
        userItemNotifyService.updateNotifiedAtByItemId(item.getId(), notifiedAt);
    }


    private void notifyAndRemoveNotify(ItemDto.detail item, String title, String contents) {
        globalNotifier.notifyUsers(getNotifyUsersByItemId(item.getId()), title, item.getItemImage(), contents);
        userItemNotifyService.deleteNotifiesByItemId(item.getId());
    }

    private List<NotifyUserVo> getNotifyUsersByItemId(Long itemId) {
        final List<UserDto.detail> users = userItemNotifyService.findNotifyUsersByItemIdAndUserStatus(itemId, UserStatus.ALIVE);
        return users.stream()
                .map(user -> new NotifyUserVo(user.getUsername(), user.getEmail(), user.getTelegramId()))
                .collect(toList());
    }

    private void logging(ItemDto.detail item) {
        notifyLogService.insertNewNotifyLog(NotifyLogDto.createByItem(item));
    }


}
