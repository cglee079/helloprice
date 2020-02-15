package com.podo.helloprice.crawl.agent.job.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Component
public class CustomBatchConfigurer implements BatchConfigurer {

    private PlatformTransactionManager transactionManager;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;

    @Autowired
    public CustomBatchConfigurer(EntityManagerFactory entityManagerFactory, CustomMapJobRepositoryFactoryBean mapJobRepositoryFactoryBean) throws Exception {

        if (entityManagerFactory == null) {
            throw new Exception("EntityManagerFactory is null!");
        }
        this.transactionManager = new JpaTransactionManager(entityManagerFactory);

        mapJobRepositoryFactoryBean.afterPropertiesSet();
        this.jobRepository = mapJobRepositoryFactoryBean.getObject();

        final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        this.jobLauncher = jobLauncher;

        final MapJobExplorerFactoryBean mapJobExplorerFactoryBean = new MapJobExplorerFactoryBean(mapJobRepositoryFactoryBean);
        mapJobExplorerFactoryBean.afterPropertiesSet();
        this.jobExplorer = mapJobExplorerFactoryBean.getObject();
    }


    @Override
    public JobRepository getJobRepository() throws Exception {
        return this.jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() throws Exception {
        return this.transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return this.jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() throws Exception {
        return this.jobExplorer;
    }
}

