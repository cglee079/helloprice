package com.podo.helloprice.telegram.client.menu.itemdelete;

import com.podo.helloprice.telegram.client.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemDeleteCommand implements Command {


    EXIT("나가기");

    private String value;

    public static ItemDeleteCommand from(String str) {
        return Command.from(values(), str, null);
    }


}
