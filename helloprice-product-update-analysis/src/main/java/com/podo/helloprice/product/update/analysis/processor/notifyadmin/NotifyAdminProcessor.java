package com.podo.helloprice.product.update.analysis.processor.notifyadmin;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.Processor;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.podo.helloprice.core.enums.ProductUpdateStatus.UPDATE_DEAD;
import static com.podo.helloprice.core.enums.ProductUpdateStatus.UPDATE_UNKNOWN;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotifyAdminProcessor implements Processor {

    private static final List<ProductUpdateStatus> NOTIFY_ADMIN_PRODUCT_UPDATES = Arrays.asList(UPDATE_DEAD, UPDATE_UNKNOWN);

    @Value("${infra.telegram.admin.id}")
    private String adminTelegramId;

    private final Notifier notifier;

    @Override
    public void process(Long productId, ProductUpdateStatus updateStatus, LocalDateTime now) {
        log.debug("PROCESSOR :: NOTIFY ADMIN :: ID : {}, UPDATE : {}, DATETIME : {}", productId, updateStatus, now);

        if (NOTIFY_ADMIN_PRODUCT_UPDATES.contains(updateStatus)) {
            notifier.notify(TelegramNotifyMessage.create(adminTelegramId, null, createContents(productId, updateStatus)));
        }
    }

    private String createContents(Long productId, ProductUpdateStatus updateStatus) {

        return new StringBuilder()
                .append("#상품 갱신 실패..!\n\n")

                .append("상품 ID : ")
                .append(productId)
                .append("\n")

                .append("상태 정보 : ")
                .append(updateStatus)
                .toString();
    }
}
