package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.core.domain.item.vo.CrawledItem;
import com.podo.helloprice.crawl.core.vo.LastPublishedItem;
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
public class CrawlJobConfig {

    public static final String CRAWL_JOB_BEAN_NAME = "crawlWorkerJob";
    private static final String CRAWL_JOB_NAME = "crawl_worker_job";
    private static final Integer CHUNK_SIZE = 1;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CrawlJobReader crawlJobReader;
    private final CrawlJobProcessor crawlJobProcessor;
    private final CrawlJobWriter crawlJobWriter;

    @Bean(CRAWL_JOB_BEAN_NAME)
    public Job job() {
        return jobBuilderFactory.get(CRAWL_JOB_NAME)
                .start(step())
                .build();

    }

    @Bean(CRAWL_JOB_BEAN_NAME + "Step")
    public Step step() {
        return stepBuilderFactory.get(CRAWL_JOB_NAME + "_step")
                .<LastPublishedItem, CrawledItem>chunk(CHUNK_SIZE)
                .reader(crawlJobReader)
                .processor(crawlJobProcessor)
                .writer(crawlJobWriter)
                .build();

    }

    @Bean
    @JobScope
    public CrawlJobParameter jobParameter() {
        return new CrawlJobParameter();
    }

}
