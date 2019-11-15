package com.podo.helloprice.telegram.client.menu.itemsearchresult;

import com.podo.helloprice.telegram.client.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemSearchResultCommand implements Command {


    EXIT("나가기");

    private String value;

    public static ItemSearchResultCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
