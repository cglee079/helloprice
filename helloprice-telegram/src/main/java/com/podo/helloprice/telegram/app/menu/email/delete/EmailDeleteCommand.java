package com.podo.helloprice.telegram.app.menu.email.delete;

import com.podo.helloprice.telegram.app.menu.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EmailDeleteCommand implements Command {


    YES("네"),
    NO("아니오");

    private String value;

    public static EmailDeleteCommand from(String str) {
        return Command.from(values(), str, null);
    }

    @Override
    public String getValue() {
        return value;
    }
}
