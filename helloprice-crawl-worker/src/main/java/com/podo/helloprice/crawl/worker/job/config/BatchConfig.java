package com.podo.helloprice.crawl.worker.job.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

//    @Bean
//    public MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean() throws Exception {
//        final MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(new ResourcelessTransactionManager());
//        mapJobRepositoryFactoryBean.afterPropertiesSet();
//        return mapJobRepositoryFactoryBean;
//    }

}
