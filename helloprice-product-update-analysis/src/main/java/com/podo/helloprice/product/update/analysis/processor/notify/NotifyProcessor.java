package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.tuser.TUserDto;
import com.podo.helloprice.product.update.analysis.domain.tusernotify.application.TUserNotifyUpdateService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.EmailNotifyPublisher;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.TelegramNotifyPublisher;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.NotifyExecutor;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.EmailContentCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class NotifyProcessor implements Processor {

    private final TelegramNotifyPublisher telegramNotifyPublisher;
    private final EmailNotifyPublisher emailNotifyPublisher;
    private final TUserNotifyUpdateService tUserNotifyUpdateService;
    private final Map<ProductUpdateStatus, NotifyExecutor> notifyExecutors;

    public NotifyProcessor(
            TelegramNotifyPublisher telegramNotifyPublisher,
            EmailNotifyPublisher emailNotifyPublisher,
            TUserNotifyUpdateService TUserNotifyUpdateService,
            List<NotifyExecutor> notifyExecutors) {

        this.telegramNotifyPublisher = telegramNotifyPublisher;
        this.emailNotifyPublisher = emailNotifyPublisher;
        this.tUserNotifyUpdateService = TUserNotifyUpdateService;
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

        tUserNotifyUpdateService.updateNotifiedAtByProductId(productId, now);

    }


    private void notifyToUsers(NotifyTarget notifyTarget) {
        final List<TUserDto> users = notifyTarget.getUsers();
        final String imageUrl = notifyTarget.getImageUrl();
        final String title = notifyTarget.getTitle();
        final String contents = notifyTarget.getContents();

        for (TUserDto user : users) {
            telegramNotifyPublisher.publish(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
            final String email = user.getEmail();

            if (!StringUtils.isEmpty(email)) {
                emailNotifyPublisher.publish(EmailNotifyMessage.create(email, user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
            }

        }
    }
}
