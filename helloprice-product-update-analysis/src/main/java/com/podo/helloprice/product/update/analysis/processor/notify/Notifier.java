package com.podo.helloprice.product.update.analysis.processor.notify;

import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.EmailNotifyPublisher;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.TelegramNotifyPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class Notifier {

    private final TelegramNotifyPublisher telegramNotifyPublisher;
    private final EmailNotifyPublisher emailNotifyPublisher;

    public void notify(TelegramNotifyMessage telegramNotifyMessage){
        telegramNotifyPublisher.publish(telegramNotifyMessage);
    }

    public void notify(EmailNotifyMessage emailNotifyMessage){

        if(StringUtils.isEmpty(emailNotifyMessage.getEmail())){
            return;
        }

        emailNotifyPublisher.publish(emailNotifyMessage);
    }

}
