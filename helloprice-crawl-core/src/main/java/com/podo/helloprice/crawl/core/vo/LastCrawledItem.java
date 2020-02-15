package com.podo.helloprice.crawl.core.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class LastCrawledItem {

    private String itemName;
    private String itemCode;

    public LastCrawledItem(String itemName, String itemCode) {
        this.itemName = itemName;
        this.itemCode = itemCode;
    }


}
