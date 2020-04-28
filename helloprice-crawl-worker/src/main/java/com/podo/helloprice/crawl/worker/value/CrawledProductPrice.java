package com.podo.helloprice.crawl.worker.value;

import lombok.Getter;

@Getter
public class CrawledProductPrice {
    private Integer price;
    private String additionalInfo;

    public CrawledProductPrice(Integer price) {
        this(price, "");
    }

    public CrawledProductPrice(Integer price, String additionalInfo) {
      this.price = price;
      this.additionalInfo = additionalInfo;
    }
}
