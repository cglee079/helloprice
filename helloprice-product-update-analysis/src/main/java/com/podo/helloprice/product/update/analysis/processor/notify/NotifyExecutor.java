package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.core.model.ProductUpdateStatus;

public interface NotifyExecutor {

    ProductUpdateStatus getProductUpdateStatus();

    boolean execute(Long productId);
}
