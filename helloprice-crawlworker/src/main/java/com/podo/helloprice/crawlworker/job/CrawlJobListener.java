package com.podo.helloprice.crawlworker.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CrawlJobListener implements JobExecutionListener {

    private final MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // InMemory Job log clear;
        mapJobRepositoryFactoryBean.clear();
    }
}
