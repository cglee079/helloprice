package com.podo.itemwatcher.pooler;

import com.podo.itemwatcher.pooler.domain.ItemInfoVo;

public interface Pooler {

    ItemInfoVo poolItem(String itemCode);
}
