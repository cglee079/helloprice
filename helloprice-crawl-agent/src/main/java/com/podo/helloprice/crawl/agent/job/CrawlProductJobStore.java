package com.podo.helloprice.crawl.agent.job;

import java.util.ArrayList;
import java.util.List;

public class CrawlProductJobStore{

    private final List<ProductUpdate> productUpdates = new ArrayList<>();

    public void addNotifyEvent(ProductUpdate productUpdate){
        this.productUpdates.add(productUpdate);
    }

    public List<ProductUpdate> getProductUpdates() {
        return new ArrayList<>(productUpdates);
    }
}
