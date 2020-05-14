package com.podo.helloprice.product.update.analysis.processor.clear;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import com.podo.helloprice.product.update.analysis.domain.tusernotify.application.TUserNotifyDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
@Component
public class NotifyClearProcessor implements Processor {

    private final TUserNotifyDeleteService TUserNotifyDeleteService;

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now) {

        switch (updateStatus){
            case UPDATE_UNKNOWN:
            case UPDATE_DEAD:
            case UPDATE_DISCONTINUE:
            case UPDATE_NOT_SUPPORT:
                TUserNotifyDeleteService.deleteNotifiesByProductId(productId);
            case UPDATE_SALE_NORMAL_PRICE:
            case UPDATE_SALE_CARD_PRICE:
            case UPDATE_SALE_CASH_PRICE:
            case UPDATE_EMPTY_AMOUNT:
            default:
                break;
        }

    }
}
