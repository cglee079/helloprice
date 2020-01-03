package com.podo.helloprice.crawlworker.job.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

/**
 * Remove Logging In Database
 * Logging -> NO INSERT DB
 * Transactional -> JPA
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class CustomBatchConfigurer implements BatchConfigurer {

    private final JpaTransactionManager jpaTransactionManager;
    private final MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean;


    @Override
    public JobRepository getJobRepository() throws Exception {
        return mapJobRepositoryFactoryBean.getObject();
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.jpaTransactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() throws Exception {
        MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(mapJobRepositoryFactoryBean);
        jobExplorerFactory.afterPropertiesSet();
        return jobExplorerFactory.getObject();
    }


}

