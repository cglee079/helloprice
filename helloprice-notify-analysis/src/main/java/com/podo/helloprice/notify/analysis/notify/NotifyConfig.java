package com.podo.helloprice.notify.analysis.notify;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.notify.analysis.notify.generator.TelegramNotifyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class NotifyConfig {

    @Bean
    public Map<ProductUpdateStatus, TelegramNotifyGenerator> telegramNotifyGenerators(List<TelegramNotifyGenerator> telegramNotifyGenerators){
        return telegramNotifyGenerators.stream()
                .collect(Collectors.toMap(TelegramNotifyGenerator::getProductUpdateStatus, t -> t));
    }
}
