package com.podo.itemwatcher.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaDefaultConfig {

    @Bean
    @ConditionalOnMissingBean
    AuditorAware auditorAware() {
        return (AuditorAware<String>) () -> Optional.of("");
    }
}
