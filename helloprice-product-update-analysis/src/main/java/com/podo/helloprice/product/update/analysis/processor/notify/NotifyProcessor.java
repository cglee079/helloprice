package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(-1)
@Component
public class NotifyProcessor implements Processor {

    private final Map<ProductUpdateStatus, NotifyExecutor> notifyExecutors;

    public NotifyProcessor(List<NotifyExecutor> notifyExecutors){
        this.notifyExecutors = notifyExecutors.stream()
                .collect(Collectors.toMap(NotifyExecutor::getProductUpdateStatus, t -> t));
    }

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus) {
        notifyExecutors.get(updateStatus).execute(productId);
    }
}
