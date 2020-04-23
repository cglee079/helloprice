package com.podo.helloprice.product.update.analysis.processor.notify.executor.common;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductSimpleDto;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.AbstractCommonNotifyExecutor;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.ProductSimpleDescribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotSupportNotifyExecutor extends AbstractCommonNotifyExecutor {


    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return ProductUpdateStatus.UPDATE_NOT_SUPPORT;
    }

    @Override
    protected String getNotifyTitle(ProductSimpleDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품은 '가격비교중지' 상태로 변경되었습니다")
                .toString();
    }

    @Override
    protected String getNotifyContents(ProductSimpleDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '가격비교중지' 상태로 변경되었습니다\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(ProductSimpleDescribe.descProductSimple(product))
                .toString();
    }

}
