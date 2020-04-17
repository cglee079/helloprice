package com.podo.helloprice.product.update.analysis.processor;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.notify.vo.NotifyDetail;

public interface Processor {

    void process(Long productId, ProductUpdateStatus updateStatus);

}
