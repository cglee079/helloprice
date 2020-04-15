package com.podo.helloprice.telegram.app.menu.product.searchselect;

import com.podo.helloprice.telegram.app.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductSearchSelectCommand implements Command {


    EXIT("나가기");

    private String value;

    public static ProductSearchSelectCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
