package com.podo.helloprice.crawlworker.job;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.ItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class CrawlJobConfig {

    @Value("${item.crawl.expire.time}")
    private Integer itemCrawlExpireTime;

    static final String CRAWL_JOB_BEAN_NAME = "crawlWorkerJob";
    private static final String CRAWL_JOB_NAME = "crawl_worker_job";
    private static final Integer CHUNK_SIZE = 1;

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CrawlJobProcessor crawlJobProcessor;
    private final CrawlJobParameter jobParameter;

    @Bean(CRAWL_JOB_BEAN_NAME)
    public Job job() {
        return jobBuilderFactory.get(CRAWL_JOB_NAME)
                .start(step())
                .build();

    }

    @Bean(CRAWL_JOB_BEAN_NAME + "Step")
    public Step step() {
        return stepBuilderFactory.get(CRAWL_JOB_NAME + "_step")
                .<Item, Item>chunk(CHUNK_SIZE)
                .reader(crawlJobReader())
                .processor(crawlJobProcessor)
                .writer(crawlJobWriter())
                .build();

    }

    @Bean
    @StepScope
    public CrawlJobParameter jobParameter() {
        return new CrawlJobParameter();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader crawlJobReader() {
        final JpaPagingItemReaderBuilder jpaPagingItemReaderBuilder = new JpaPagingItemReaderBuilder();

        final Map<String, Object> params = new HashMap<>();
        params.put("itemStatus", ItemStatus.ALIVE);
        params.put("expirePoolAt", jobParameter.getCreateAt().minusMinutes(itemCrawlExpireTime));

        final String query = "" +
                "SELECT i " +
                "FROM Item i " +
                "WHERE i.itemStatus = :itemStatus " +
                "AND i.lastCrawledAt <= :expirePoolAt " +
                "ORDER BY i.lastCrawledAt ";

        return jpaPagingItemReaderBuilder.name(CRAWL_JOB_NAME + "_reader")
                .queryString(query)
                .maxItemCount(CHUNK_SIZE)
                .parameterValues(params)
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    @StepScope
    public JpaItemWriter crawlJobWriter() {
        return new JpaItemWriterBuilder()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


}
