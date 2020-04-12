package com.podo.helloprice.telegram.app.menu.productserachadd;

import com.podo.helloprice.telegram.app.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductSearchAddCommand implements Command {


    YES("네  "),
    NO("아니오");

    public static final Integer COMMAND_LENGTH = 3;
    private String value;

    public static ProductSearchAddCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
