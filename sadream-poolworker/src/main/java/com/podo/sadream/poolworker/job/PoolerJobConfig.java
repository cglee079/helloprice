package com.podo.sadream.poolworker.job;

import com.podo.sadream.core.domain.item.Item;
import com.podo.sadream.core.domain.item.ItemStatus;
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
public class PoolerJobConfig {

    @Value("${item.pool.expire.time}")
    private Integer itemPoolExpireTime;

    public static final String POOLER_JOB_BEAN_NAME = "poolWorkerJob";
    public static final String POOLER_JOB_NAME = "pool_worker_job";
    public static final Integer CHUNK_SIZE = 1;

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PoolerJobProcessor poolerJobProcessor;
    private final PoolerJobParameter jobParameter;

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
    public PoolerJobParameter jobParameter() {
        return new PoolerJobParameter();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader poolerJobReader() {
        final JpaPagingItemReaderBuilder jpaPagingItemReaderBuilder = new JpaPagingItemReaderBuilder();

        Map<String, Object> params = new HashMap<>();
        params.put("itemStatus", ItemStatus.BE);
        params.put("expirePoolAt", jobParameter.getCreateAt().minusMinutes(itemPoolExpireTime));

        final String query = "" +
                "SELECT i " +
                "FROM Item i " +
                "WHERE i.itemStatus = :itemStatus " +
                "AND i.lastPoolAt <= :expirePoolAt " +
                "ORDER BY i.lastPoolAt ";

        return jpaPagingItemReaderBuilder.name(POOLER_JOB_NAME + "_reader")
                .queryString(query)
                .maxItemCount(CHUNK_SIZE)
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
