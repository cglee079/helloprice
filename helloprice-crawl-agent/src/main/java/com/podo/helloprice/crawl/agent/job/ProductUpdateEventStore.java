package com.podo.helloprice.crawl.agent.job;

import com.podo.helloprice.core.enums.ProductUpdateStatus;

import java.util.ArrayList;
import java.util.List;

public class ProductUpdateEventStore {

    private final List<ProductUpdateEvent> productUpdateEvents = new ArrayList<>();

    public void add(ProductUpdateEvent productUpdateEvent){
        this.productUpdateEvents.add(productUpdateEvent);
    }

    public void addAll(List<ProductUpdateEvent> updatePrices) {
        this.productUpdateEvents.addAll(updatePrices);
    }

    public List<ProductUpdateEvent> getProductUpdateEvents() {
        return new ArrayList<>(productUpdateEvents);
    }


}
