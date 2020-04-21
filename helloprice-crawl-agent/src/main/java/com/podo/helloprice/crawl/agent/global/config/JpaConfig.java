package com.podo.helloprice.crawl.agent.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    @Bean
    AuditorAware auditorAware() {
        return (AuditorAware<String>) () -> Optional.of("CrawlAgent");
    }
}
