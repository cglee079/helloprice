package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.crawl.core.vo.LastPublishedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlJobReader extends AbstractItemCountingItemStreamItemReader<LastPublishedItem> {

    private int count = 0;
    private final CrawlJobParameter jobParameter;

    @Override
    protected LastPublishedItem doRead() {
        if(count > 0){
            return null;
        }

        count++;

        return jobParameter.getLastPublishedItem();
    }

    @Override
    protected void doOpen(){
        this.count = 0;
        this.setName("crawlJobReader");
    }

    @Override
    protected void doClose() {

    }

}
