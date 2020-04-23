package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.crawl.agent.job.step.crawl.CrawlProductUpdateJobProcessor;
import com.podo.helloprice.crawl.agent.job.step.crawl.CrawlProductUpdateJobReader;
import com.podo.helloprice.crawl.agent.job.step.crawl.CrawlProductUpdateJobWriter;
import com.podo.helloprice.crawl.agent.job.step.publish.CrawlProductUpdatePublishTasklet;
import com.podo.helloprice.crawl.worker.value.CrawledProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CrawlProductJobConfig {

    public static final String CRAWL_JOB_BEAN_NAME = "crawlProductJob";
    private static final String CRAWL_JOB_NAME = "crawl_product_job";
    private static final String CRAWL_PRODUCT_UPDATE_STEP_BEAN_NAME = CRAWL_JOB_BEAN_NAME + "UpdateStep";
    private static final String CRAWL_PRODUCT_UPDATE_STEP_NAME = CRAWL_JOB_NAME + "_update_step";
    private static final String CRAWL_PRODUCT_NOTIFY_STEP_BEAN_NAME = CRAWL_JOB_BEAN_NAME + "NotifyStep";
    private static final String CRAWL_PRODUCT_NOTIFY_STEP_NAME = CRAWL_JOB_NAME + "_notify_step";
    private static final Integer CHUNK_SIZE = 1;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CrawlProductUpdateJobReader crawlProductUpdateJobReader;
    private final CrawlProductUpdateJobProcessor crawlProductUpdateJobProcessor;
    private final CrawlProductUpdateJobWriter crawlProductUpdateJobWriter;
    private final CrawlProductUpdatePublishTasklet crawlProductUpdatePublishTasklet;

    @Bean(CRAWL_JOB_BEAN_NAME)
    public Job job() {
        return jobBuilderFactory.get(CRAWL_JOB_NAME)
                .start(crawlProductUpdateStep())
                .next(crawlProductNotifyStep())
                .build();
    }

    @Bean(CRAWL_PRODUCT_UPDATE_STEP_BEAN_NAME)
    public Step crawlProductUpdateStep() {
        return stepBuilderFactory.get(CRAWL_PRODUCT_UPDATE_STEP_NAME)
                .<ProductToCrawl, CrawledProduct>chunk(CHUNK_SIZE)
                .reader(crawlProductUpdateJobReader)
                .processor(crawlProductUpdateJobProcessor)
                .writer(crawlProductUpdateJobWriter)
                .build();
    }

    @Bean(CRAWL_PRODUCT_NOTIFY_STEP_BEAN_NAME)
    public Step crawlProductNotifyStep() {
        return stepBuilderFactory.get(CRAWL_PRODUCT_NOTIFY_STEP_NAME)
                .tasklet(crawlProductUpdatePublishTasklet)
                .build();
    }

    @Bean
    @JobScope
    public CrawlProductJobParameter jobParameter() {
        return new CrawlProductJobParameter();
    }

    @Bean
    @JobScope
    public CrawlProductJobStore jobStore() {
        return new CrawlProductJobStore();
    }
}
