package com.podo.helloprice.crawl.worker.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class ProductSearchVo {
    private String productCode;
    private String description;

    public ProductSearchVo(String productCode, String description) {
        this.productCode = productCode;
        this.description = description;
    }
}
