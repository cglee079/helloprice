package com.podo.helloprice.telegram.app.menu.product.delete;

import com.podo.helloprice.telegram.app.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductDeleteCommand implements Command {


    EXIT("나가기");

    private String value;

    public static ProductDeleteCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
