package com.podo.helloprice.crawl.agent.job;

import java.util.ArrayList;
import java.util.List;

public class ProductUpdateEventStore {

    private final List<ProductUpdateEvent> productUpdateEvents = new ArrayList<>();

    public void addProductUpdateEvent(ProductUpdateEvent productUpdateEvent){
        this.productUpdateEvents.add(productUpdateEvent);
    }

    public List<ProductUpdateEvent> getProductUpdateEvents() {
        return new ArrayList<>(productUpdateEvents);
    }
}
