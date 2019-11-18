package com.podo.helloprice.pooler;


import com.podo.helloprice.core.domain.item.CrawledItemVo;

public interface Pooler {

    CrawledItemVo poolItem(String itemCode);
}
