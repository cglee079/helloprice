package com.podo.helloprice.crawl.worker.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class ProductSearchVo {
    private String itemCode;
    private String itemDesc;

    public ProductSearchVo(String itemCode, String itemDesc) {
        this.itemCode = itemCode;
        this.itemDesc = itemDesc;
    }
}
