package com.podo.helloprice.product.update.analysis.processor.notify.executor.sale;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.SaleNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.model.PriceType.CASH;
import static com.podo.helloprice.core.model.ProductUpdateStatus.UPDATE_SALE_CASH_PRICE;

@RequiredArgsConstructor
@Component
public class SaleOfCashNotifyExecutor extends SaleNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_CASH_PRICE;
    }

    @Override
    protected PriceType getPriceType() {
        return CASH;
    }
}
