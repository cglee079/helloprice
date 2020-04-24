package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.notifylog.application.NotifyLogInsertService;
import com.podo.helloprice.product.update.analysis.domain.notifylog.dto.NotifyLogInsertDto;
import com.podo.helloprice.product.update.analysis.domain.user.UserDto;
import com.podo.helloprice.product.update.analysis.domain.userproduct.application.UserProductNotifyUpdateService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.NotifyExecutor;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.EmailContentCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Order(-1)
@Component
public class NotifyProcessor implements Processor {

    private final Notifier notifier;

    private final NotifyLogInsertService notifyLogInsertService;
    private final UserProductNotifyUpdateService userProductNotifyUpdateService;
    private final Map<ProductUpdateStatus, NotifyExecutor> notifyExecutors;

    public NotifyProcessor(
            Notifier notifier,
            NotifyLogInsertService notifyLogInsertService,
            UserProductNotifyUpdateService userProductNotifyUpdateService,
            List<NotifyExecutor> notifyExecutors) {

        this.notifier = notifier;
        this.notifyLogInsertService = notifyLogInsertService;
        this.userProductNotifyUpdateService = userProductNotifyUpdateService;
        this.notifyExecutors = notifyExecutors.stream()
                .collect(toMap(NotifyExecutor::getProductUpdateStatus, t -> t));
    }

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now) {
        log.debug("PROCESSOR :: NOTIFY :: ID : {}, UPDATE : {}, DATETIME : {}", productId, updateStatus, now);

        final NotifyTarget notifyTarget = notifyExecutors.get(updateStatus).execute(productId);

        if (notifyTarget.equals(NotifyTarget.EMPTY)) {
            return;
        }

        notifyToUsers(notifyTarget);

        userProductNotifyUpdateService.updateNotifiedAtByProductId(productId, now);

        final NotifyLogInsertDto notifyLog = NotifyLogInsertDto.builder()
                .productId(productId)
                .updateStatus(updateStatus)
                .imageUrl(notifyTarget.getImageUrl())
                .title(notifyTarget.getTitle())
                .contents(notifyTarget.getContents())
                .build();

        notifyLogInsertService.insertNew(notifyLog);
    }


    private void notifyToUsers(NotifyTarget notifyTarget) {
        final List<UserDto> users = notifyTarget.getUsers();
        final String imageUrl = notifyTarget.getImageUrl();
        final String title = notifyTarget.getTitle();
        final String contents = notifyTarget.getContents();

        for (UserDto user : users) {
            notifier.notify(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
            final String email = user.getEmail();

            if (!StringUtils.isEmpty(email)) {
                notifier.notify(EmailNotifyMessage.create(email, user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
            }

        }
    }
}
