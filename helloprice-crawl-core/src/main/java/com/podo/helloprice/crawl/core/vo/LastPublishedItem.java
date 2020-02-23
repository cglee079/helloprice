package com.podo.helloprice.crawl.core.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class LastPublishedItem {

    private String itemName;
    private String itemCode;

    public LastPublishedItem(String itemName, String itemCode) {
        this.itemName = itemName;
        this.itemCode = itemCode;
    }


}
