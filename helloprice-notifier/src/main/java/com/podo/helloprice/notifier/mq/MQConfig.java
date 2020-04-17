package com.podo.helloprice.notifier.mq;

import com.podo.helloprice.notifier.mq.consumer.TelegramNotifyMessageConsumer;
import com.podo.helloprice.notifier.mq.message.TelegramNotifyMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MQConfig {

    @Bean
    public Consumer<TelegramNotifyMessage> consumeTelegramNotify(TelegramNotifyMessageConsumer telegramNotifyMessageConsumer) {
        return telegramNotifyMessageConsumer;
    }

}
