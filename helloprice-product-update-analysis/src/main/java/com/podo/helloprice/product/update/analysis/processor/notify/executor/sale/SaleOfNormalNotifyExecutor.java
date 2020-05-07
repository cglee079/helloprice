package com.podo.helloprice.product.update.analysis.processor.notify.executor.sale;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.AbstractSaleNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.enums.SaleType.NORMAL;
import static com.podo.helloprice.core.enums.ProductUpdateStatus.UPDATE_SALE_NORMAL_PRICE;

@RequiredArgsConstructor
@Component
public class SaleOfNormalNotifyExecutor extends AbstractSaleNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_NORMAL_PRICE;
    }

    @Override
    protected SaleType getPriceType() {
        return NORMAL;
    }
}
