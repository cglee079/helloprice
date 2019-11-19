package com.podo.helloprice.crawlworker.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlJobWorker implements Worker {

    private final JobLauncher jobLauncher;

    @Qualifier(value = CrawlJobConfig.CRAWL_JOB_BEAN_NAME)
    private final Job crawlJob;

    public void run() {
        log.info("상품 정보 갱신 작업을 실행합니다");

        try {

            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addDate("createAt", new Date())
                            .toJobParameters();

            jobLauncher.run(crawlJob, jobParameters);

        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            log.error("Pooler Job Run Error");

        }

    }
}
