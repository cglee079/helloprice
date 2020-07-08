package com.podo.helloprice.telegram.app.menu.product.typeselect;

import com.podo.helloprice.telegram.app.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductTypeSelectCommand implements Command {

    EXIT("나가기");

    private String value;

    public static ProductTypeSelectCommand from(String str) {
        return Command.from(values(), str, null);
    }
}
