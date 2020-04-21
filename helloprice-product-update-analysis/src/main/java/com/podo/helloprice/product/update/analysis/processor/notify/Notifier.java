package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.core.util.StringUtil;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.EmailNotifyPublisher;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.TelegramNotifyPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class Notifier {

    private final TelegramNotifyPublisher telegramNotifyPublisher;
    private final EmailNotifyPublisher emailNotifyPublisher;

    public void notify(TelegramNotifyMessage telegramNotifyMessage){
        log.info("NOTIFY ::  TELEGRAM :: telegramId : {}, contents : {}", telegramNotifyMessage.getTelegramId(), StringUtil.summary(telegramNotifyMessage.getContents(), 100));
        telegramNotifyPublisher.publish(telegramNotifyMessage);
    }

    public void notify(EmailNotifyMessage emailNotifyMessage){
        if(StringUtils.isEmpty(emailNotifyMessage.getEmail())){
            return;
        }

        log.info("NOTIFY ::  EMAIL :: email : {}, contents : {}", emailNotifyMessage.getEmail(), StringUtil.summary(emailNotifyMessage.getContents(), 100));
        emailNotifyPublisher.publish(emailNotifyMessage);
    }

}
