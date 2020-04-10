package com.podo.helloprice.crawl.agent.job.step;

import com.podo.helloprice.crawl.agent.global.infra.mq.message.LastPublishedProduct;
import com.podo.helloprice.crawl.agent.job.CrawlProductJobParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlProductJobReader extends AbstractItemCountingItemStreamItemReader<LastPublishedProduct> {

    private int count = 0;
    private final CrawlProductJobParameter jobParameter;

    @Override
    protected LastPublishedProduct doRead() {
        if (isEnded()) {
            return null;
        }

        count++;

        return jobParameter.getLastPublishedProduct();
    }

    private boolean isEnded() {
        return count >= 1;
    }

    @Override
    protected void doOpen() {
        this.setName(this.getClass().getName());
        this.count = 0;
    }

    @Override
    protected void doClose() {

    }

}
