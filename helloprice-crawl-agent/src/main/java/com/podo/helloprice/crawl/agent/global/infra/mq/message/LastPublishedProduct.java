package com.podo.helloprice.crawl.agent.global.infra.mq.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class LastPublishedProduct {

    private String productName;
    private String productCode;

    public LastPublishedProduct(String productName, String productCode) {
        this.productName = productName;
        this.productCode = productCode;
    }
}
