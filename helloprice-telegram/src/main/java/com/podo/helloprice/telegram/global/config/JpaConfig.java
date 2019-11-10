package com.podo.helloprice.telegram.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class JpaConfig {

    @Bean
    AuditorAware auditorAware() {
        return (AuditorAware<String>) () -> Optional.of("Helloprice.Telegram@" + Thread.currentThread());
    }
}
