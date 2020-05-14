package com.podo.helloprice.product.update.analysis.processor.pushnotify.executor.common;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDto;
import com.podo.helloprice.product.update.analysis.processor.pushnotify.executor.AbstractCommonPushNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotSupportPushNotifyExecutor extends AbstractCommonPushNotifyExecutor {


    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return ProductUpdateStatus.UPDATE_NOT_SUPPORT;
    }

    @Override
    protected String getContents(ProductDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품은 '가격비교중지' 상태로 변경되었습니다")
                .toString();
    }


}
