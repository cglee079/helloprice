package com.podo.helloprice.crawl.agent.job.step.publish;

import com.podo.helloprice.crawl.agent.global.infra.mq.publish.ProductUpdatePublisher;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.ProductUpdateMessage;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEventStore;
import com.podo.helloprice.crawl.agent.job.ProductUpdateEvent;
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

    private final ProductUpdateEventStore productUpdateEventStore;
    private final ProductUpdatePublisher productUpdatePublisher;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        final List<ProductUpdateEvent> productUpdateEvents = productUpdateEventStore.getProductUpdateEvents();

        for (ProductUpdateEvent productUpdateEvent : productUpdateEvents) {
            productUpdatePublisher.publish(new ProductUpdateMessage(productUpdateEvent.getProductId(), productUpdateEvent.getUpdateStatus()));
        }

        return RepeatStatus.FINISHED;
    }
}
