package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.notifylog.application.NotifyLogInsertService;
import com.podo.helloprice.product.update.analysis.domain.notifylog.dto.NotifyLogInsertDto;
import com.podo.helloprice.product.update.analysis.domain.userproduct.application.UserProductNotifyUpdateService;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.NotifyExecutor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Order(-1)
@Component
public class NotifyProcessor implements Processor {

    private final NotifyLogInsertService notifyLogInsertService;
    private final UserProductNotifyUpdateService userProductNotifyUpdateService;
    private final Map<ProductUpdateStatus, NotifyExecutor> notifyExecutors;

    public NotifyProcessor(
            NotifyLogInsertService notifyLogInsertService,
            UserProductNotifyUpdateService userProductNotifyUpdateService,
            List<NotifyExecutor> notifyExecutors) {

        this.notifyLogInsertService = notifyLogInsertService;
        this.userProductNotifyUpdateService = userProductNotifyUpdateService;
        this.notifyExecutors = notifyExecutors.stream()
                .collect(toMap(NotifyExecutor::getProductUpdateStatus, t -> t));
    }

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now) {

        if(notifyExecutors.get(updateStatus).execute(productId)){
            userProductNotifyUpdateService.updateNotifiedAtByProductId(productId, now);
            notifyLogInsertService.insertNew(new NotifyLogInsertDto(productId, updateStatus, now));
        }

    }
}
