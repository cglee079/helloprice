package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.NotifyTarget;

public interface NotifyExecutor {

    ProductUpdateStatus getProductUpdateStatus();

    NotifyTarget execute(Long notifyTarget);
}
