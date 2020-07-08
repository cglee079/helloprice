package com.podo.helloprice.product.update.analysis.processor.pushnotify;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.usernotify.application.UserNotifyUpdateService;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import com.podo.helloprice.product.update.analysis.processor.pushnotify.executor.PushNotifyExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
//TODO
//@Component
public class PushNotifyProcessor implements Processor {

    private final UserNotifyUpdateService userNotifyUpdateService;
    private final Map<ProductUpdateStatus, PushNotifyExecutor> notifyExecutors;

    public PushNotifyProcessor(
            UserNotifyUpdateService userNotifyUpdateService,
            List<PushNotifyExecutor> pushNotifyExecutors) {

        this.userNotifyUpdateService = userNotifyUpdateService;
        this.notifyExecutors = pushNotifyExecutors.stream()
                .collect(toMap(PushNotifyExecutor::getProductUpdateStatus, t -> t));
    }

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now) {
        log.debug("PROCESSOR :: NOTIFY :: ID : {}, UPDATE : {}, DATETIME : {}", productId, updateStatus, now);

        final PushTarget pushTarget = notifyExecutors.get(updateStatus).execute(productId);

        if (pushTarget.equals(PushTarget.EMPTY)) {
            return;
        }

        userNotifyUpdateService.updateNotifiedAtByProductId(productId, now);
    }
}
