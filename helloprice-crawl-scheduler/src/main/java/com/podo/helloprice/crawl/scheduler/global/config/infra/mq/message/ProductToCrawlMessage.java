package com.podo.helloprice.crawl.scheduler.global.config.infra.mq.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ProductToCrawlMessage {

    private String productName;
    private String productCode;

    public ProductToCrawlMessage(String productName, String productCode) {
        this.productName = productName;
        this.productCode = productCode;
    }
}
