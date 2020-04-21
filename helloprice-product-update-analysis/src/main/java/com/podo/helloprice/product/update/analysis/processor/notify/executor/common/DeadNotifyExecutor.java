package com.podo.helloprice.product.update.analysis.processor.notify.executor.common;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductSimpleDto;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.AbstractCommonNotifyExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.model.ProductUpdateStatus.UPDATE_DEAD;

@RequiredArgsConstructor
@Component
public class DeadNotifyExecutor extends AbstractCommonNotifyExecutor {

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_DEAD;
    }

    @Override
    protected String getNotifyTitle(ProductSimpleDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품은 더 이상 페이지를 확인 할 수 없습니다")
                .toString();
    }

    @Override
    protected String getNotifyContents(ProductSimpleDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품의 상품페이지를 더이상 확인 할 수 없습니다.\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(ProductSimpleDescribe.descProductSimple(product))
                .toString();
    }


}
