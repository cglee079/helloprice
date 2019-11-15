package com.podo.helloprice.core.domain.item;

import com.podo.helloprice.core.util.MyStringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@ToString
@EqualsAndHashCode
@Getter
public class ItemSearchResultVo {
    private String itemCode;
    private String itemDesc;

    public ItemSearchResultVo(String itemCode, String itemDesc) {
        this.itemCode = itemCode;
        this.itemDesc = itemDesc;
    }

    public boolean validate() {
        if (StringUtils.isEmpty(itemCode)) {
            return false;
        }

        if (StringUtils.isEmpty(itemDesc)) {
            return false;
        }

        if (!MyStringUtils.isStringInteger(itemCode)) {
            return false;
        }

        return true;
    }
}
