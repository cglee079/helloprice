package com.podo.helloprice.telegram.client.menu.itemserachadd;

import com.podo.helloprice.telegram.client.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemSearchAddCommand implements Command {


    YES("네  "),
    NO("아니오");

    private String value;

    public static ItemSearchAddCommand from(String str) {
        return Command.from(values(), str, null);
    }


}