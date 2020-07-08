package com.podo.helloprice.product.update.analysis.processor.pushnotify.executor.common;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDto;
import com.podo.helloprice.product.update.analysis.processor.pushnotify.executor.AbstractCommonPushNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.enums.ProductUpdateStatus.UPDATE_DEAD;

@RequiredArgsConstructor
@Component
public class DeadPushNotifyExecutor extends AbstractCommonPushNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_DEAD;
    }

    @Override
    protected String getContents(ProductDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품은 더 이상 페이지를 확인 할 수 없습니다")
                .toString();
    }


}
