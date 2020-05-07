package com.podo.helloprice.product.update.analysis.processor.notify.executor.sale;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.AbstractSaleNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.enums.SaleType.CASH;
import static com.podo.helloprice.core.enums.ProductUpdateStatus.UPDATE_SALE_CASH_PRICE;

@RequiredArgsConstructor
@Component
public class SaleOfCashNotifyExecutor extends AbstractSaleNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_CASH_PRICE;
    }

    @Override
    protected SaleType getSaleType() {
        return CASH;
    }
}
