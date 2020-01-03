package com.podo.helloprice.crawlworker.job.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory() throws Exception {
        final MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(jpaTransactionManager());
        mapJobRepositoryFactoryBean.afterPropertiesSet();
        return mapJobRepositoryFactoryBean;
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(){
        return new JpaTransactionManager(this.entityManagerFactory);
    }
}
