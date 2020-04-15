package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.LastPublishedProduct;
import com.podo.helloprice.crawl.agent.job.step.CrawlProductJobProcessor;
import com.podo.helloprice.crawl.agent.job.step.CrawlProductJobReader;
import com.podo.helloprice.crawl.agent.job.step.CrawlProductJobWriter;
import com.podo.helloprice.crawl.worker.vo.CrawledProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CrawlProductJobConfig {

    public static final String CRAWL_JOB_BEAN_NAME = "crawlWorkerJob";
    private static final String CRAWL_JOB_NAME = "crawl_worker_job";
    private static final String CRAWL_STEP_BEAN_NAME = CRAWL_JOB_BEAN_NAME + "Step";
    private static final String CRAWL_STEP_NAME = CRAWL_JOB_NAME + "_step";
    private static final Integer CHUNK_SIZE = 1;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CrawlProductJobReader crawlProductJobReader;
    private final CrawlProductJobProcessor crawlProductJobProcessor;
    private final CrawlProductJobWriter crawlProductJobWriter;

    @Bean(CRAWL_JOB_BEAN_NAME)
    public Job job() {
        return jobBuilderFactory.get(CRAWL_JOB_NAME)
                .start(step())
                .build();
    }

    @Bean(CRAWL_STEP_BEAN_NAME)
    public Step step() {
        return stepBuilderFactory.get(CRAWL_STEP_NAME)
                .<LastPublishedProduct, CrawledProduct>chunk(CHUNK_SIZE)
                .reader(crawlProductJobReader)
                .processor(crawlProductJobProcessor)
                .writer(crawlProductJobWriter)
                .build();
    }

    @Bean
    @JobScope
    public CrawlProductJobParameter jobParameter() {
        return new CrawlProductJobParameter();
    }

}
