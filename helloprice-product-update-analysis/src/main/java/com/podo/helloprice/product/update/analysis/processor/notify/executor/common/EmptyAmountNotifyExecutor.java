package com.podo.helloprice.product.update.analysis.processor.notify.executor.common;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductSimpleDto;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.AbstractCommonNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmptyAmountNotifyExecutor extends AbstractCommonNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return ProductUpdateStatus.UPDATE_EMPTY_AMOUNT;
    }

    @Override
    protected String getNotifyTitle(ProductSimpleDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품은 '재고없음' 상태로 변경되었습니다")
                .toString();
    }

    @Override
    protected String getNotifyContents(ProductSimpleDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '재고없음' 상태로 변경되었습니다\n")
                .append("</b>")
                .append("\n")
                .append(ProductSimpleDescribe.descProductSimple(product))
                .toString();
    }
}
