package com.podo.helloprice.crawl.agent.job.step.publish;

import com.podo.helloprice.crawl.agent.global.infra.mq.publish.ProductUpdatePublisher;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.ProductUpdateMessage;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobStore;
import com.podo.helloprice.crawl.agent.job.ProductUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CrawlProductUpdatePublishTasklet implements Tasklet {

    private final CrawlProductJobStore crawlProductJobStore;
    private final ProductUpdatePublisher productUpdatePublisher;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        final List<ProductUpdate> productUpdates = crawlProductJobStore.getProductUpdates();

        for (ProductUpdate productUpdate : productUpdates) {
            productUpdatePublisher.publish(new ProductUpdateMessage(productUpdate.getProductId(), productUpdate.getUpdateStatus()));
        }

        return RepeatStatus.FINISHED;
    }
}
