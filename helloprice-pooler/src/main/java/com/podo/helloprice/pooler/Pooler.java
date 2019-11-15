package com.podo.helloprice.pooler;


import com.podo.helloprice.core.domain.item.ItemInfoVo;

public interface Pooler {

    ItemInfoVo poolItem(String itemCode);
}
