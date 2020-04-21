package com.podo.helloprice.product.update.analysis.processor.notify.executor.common;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.CommonNotifyExecutor;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.ProductDescribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotSupportNotifyExecutor extends CommonNotifyExecutor {


    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return ProductUpdateStatus.UPDATE_NOT_SUPPORT;
    }

    @Override
    protected String getNotifyTitle(ProductDetailDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품은 '가격비교중지' 상태로 변경되었습니다")
                .toString();
    }

    @Override
    protected String getNotifyContents(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품은 '가격비교중비' 상태로 변경되었습니다\n")
                .append("</b>")
                .append("\n")
                .append(ProductDescribe.descProductDetailWithChangeMessage(product))
                .toString();
    }

}
