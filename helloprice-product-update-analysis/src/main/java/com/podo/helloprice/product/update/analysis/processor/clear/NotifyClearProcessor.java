package com.podo.helloprice.product.update.analysis.processor.clear;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import com.podo.helloprice.product.update.analysis.domain.userproduct.application.UserProductSaleNotifyDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class NotifyClearProcessor implements Processor {

    private final UserProductSaleNotifyDeleteService userProductSaleNotifyDeleteService;

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now) {

        switch (updateStatus){
            case UPDATE_UNKNOWN:
            case UPDATE_DEAD:
            case UPDATE_DISCONTINUE:
            case UPDATE_NOT_SUPPORT:
                userProductSaleNotifyDeleteService.deleteNotifiesByProductId(productId);
            case UPDATE_SALE_NORMAL_PRICE:
            case UPDATE_SALE_CARD_PRICE:
            case UPDATE_SALE_CASH_PRICE:
            case UPDATE_EMPTY_AMOUNT:
            default:
                break;
        }

    }
}
