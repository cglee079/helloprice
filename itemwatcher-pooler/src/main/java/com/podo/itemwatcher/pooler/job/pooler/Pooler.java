package com.podo.itemwatcher.pooler.job.pooler;

import com.podo.itemwatcher.core.domain.item.ItemInfoVo;

public interface Pooler {

    ItemInfoVo poolItem(String itemCode);
}
