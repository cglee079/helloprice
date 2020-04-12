package com.podo.helloprice.telegram.app.menu.productsearchresult;

import com.podo.helloprice.telegram.app.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductSearchResultCommand implements Command {


    EXIT("나가기");

    private String value;

    public static ProductSearchResultCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
