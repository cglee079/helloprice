package com.podo.itemwatcher.scheduler.job;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.ItemStatus;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class PoolerJobConfig {

    public static final String POOLER_JOB_BEAN_NAME = "poolerJob";
    public static final String POOLER_JOB_NAME = "pooler_job";
    public static final Integer CHUNK_SIZE = 1;

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PoolerJobProcessor poolerJobProcessor;

    @Bean(POOLER_JOB_BEAN_NAME)
    public Job job() {
        return jobBuilderFactory.get(POOLER_JOB_NAME)
                .start(step())
                .build();

    }

    @Bean(POOLER_JOB_BEAN_NAME + "Step")
    public Step step() {
        return stepBuilderFactory.get(POOLER_JOB_NAME + "_step")
                .<Item, Item>chunk(CHUNK_SIZE)
                .reader(poolerJobReader())
                .processor(poolerJobProcessor)
                .writer(poolerJobWriter())
                .build();
        //.listener(poolerJobWriter())
    }


    @Bean
    @StepScope
    public JpaPagingItemReader poolerJobReader() {
        final JpaPagingItemReaderBuilder jpaPagingItemReaderBuilder = new JpaPagingItemReaderBuilder();

        Map<String, Object> params = new HashMap<>();
        params.put("itemStatus", ItemStatus.SAIL);

        final String query = "SELECT i FROM Item i WHERE i.itemStatus = :itemStatus ORDER BY i.lastPoolAt";

        return jpaPagingItemReaderBuilder.name(POOLER_JOB_NAME + "_reader")
                .queryString(query)
                .parameterValues(params)
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    @StepScope
    public JpaItemWriter poolerJobWriter() {
        return new JpaItemWriterBuilder()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


}
