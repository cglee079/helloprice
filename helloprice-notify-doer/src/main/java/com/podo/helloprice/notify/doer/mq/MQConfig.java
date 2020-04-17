package com.podo.helloprice.notify.doer.mq;

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
