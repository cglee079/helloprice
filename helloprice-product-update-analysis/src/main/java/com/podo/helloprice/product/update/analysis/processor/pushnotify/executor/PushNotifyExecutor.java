package com.podo.helloprice.product.update.analysis.processor.pushnotify.executor;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.pushnotify.PushTarget;

public interface PushNotifyExecutor {

    ProductUpdateStatus getProductUpdateStatus();

    PushTarget execute(Long notifyTarget);
}
