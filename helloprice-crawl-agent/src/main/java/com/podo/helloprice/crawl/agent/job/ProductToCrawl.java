package com.podo.helloprice.crawl.agent.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductToCrawl {
    private String productCode;
    private String productName;
}
