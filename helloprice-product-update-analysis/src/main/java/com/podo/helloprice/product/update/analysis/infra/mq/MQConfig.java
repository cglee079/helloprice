package com.podo.helloprice.product.update.analysis.infra.mq;

import com.podo.helloprice.product.update.analysis.infra.mq.consumer.ProductUpdateConsumer;
import com.podo.helloprice.product.update.analysis.infra.mq.message.ProductUpdateMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.EmailNotifyPublisher;
import com.podo.helloprice.product.update.analysis.infra.mq.publisher.TelegramNotifyPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MQConfig {

    @Bean
    public Consumer<ProductUpdateMessage> consumeProductUpdate(ProductUpdateConsumer productUpdateConsumer) {
        return productUpdateConsumer;
    }

    @Bean
    public Supplier<Flux<TelegramNotifyMessage>> publishTelegramNotify(TelegramNotifyPublisher telegramNotifyPublisher){
        return telegramNotifyPublisher::processor;
    }

    @Bean
    public Supplier<Flux<TelegramNotifyMessage>> publishEmailNotify(EmailNotifyPublisher emailNotifyPublisher){
        return emailNotifyPublisher::processor;
    }
}
