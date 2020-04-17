package com.podo.helloprice.crawl.agent.job.step.notify;

import com.podo.helloprice.crawl.agent.global.infra.mq.publish.NotifyEventPublisher;
import com.podo.helloprice.crawl.agent.global.infra.mq.message.NotifyEventMessage;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobStore;
import com.podo.helloprice.crawl.agent.job.NotifyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CrawlProductNotifyTasklet implements Tasklet {

    private final CrawlProductJobStore crawlProductJobStore;
    private final NotifyEventPublisher notifyEventPublisher;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        final List<NotifyEvent> notifyEvents = crawlProductJobStore.getNotifyEvents();

        for (NotifyEvent notifyEvent : notifyEvents) {
            notifyEventPublisher.publish(new NotifyEventMessage(notifyEvent.getProductId(), notifyEvent.getProductUpdateStatus()));
        }

        return RepeatStatus.FINISHED;
    }
}
