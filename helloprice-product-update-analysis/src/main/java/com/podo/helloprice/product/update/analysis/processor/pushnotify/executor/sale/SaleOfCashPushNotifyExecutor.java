package com.podo.helloprice.product.update.analysis.processor.pushnotify.executor.sale;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.product.update.analysis.processor.pushnotify.executor.AbstractSalePushNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.enums.ProductUpdateStatus.UPDATE_SALE_CASH_PRICE;
import static com.podo.helloprice.core.enums.SaleType.CASH;

@RequiredArgsConstructor
@Component
public class SaleOfCashPushNotifyExecutor extends AbstractSalePushNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_CASH_PRICE;
    }

    @Override
    protected SaleType getSaleType() {
        return CASH;
    }
}
