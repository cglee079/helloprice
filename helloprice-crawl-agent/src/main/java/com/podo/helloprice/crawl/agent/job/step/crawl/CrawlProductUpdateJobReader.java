package com.podo.helloprice.crawl.agent.job.step.crawl;

import com.podo.helloprice.crawl.agent.job.CrawlProductJobParameter;
import com.podo.helloprice.crawl.agent.job.ProductToCrawl;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlProductUpdateJobReader extends AbstractItemCountingItemStreamItemReader<ProductToCrawl> {

    private int count = 0;
    private final CrawlProductJobParameter jobParameter;

    @Override
    protected ProductToCrawl doRead() {
        if (isEnded()) {
            return null;
        }

        count++;

        return jobParameter.getProductToCrawl();
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
